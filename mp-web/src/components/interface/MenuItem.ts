export default class MenuItem {
    menuId: string;
    menuName: string;
    // 菜单类型（1-目录 2-菜单）
    menuType: Number;
    // 地址类型（1-内部组件地址  2-外部链接地址），仅menuType=2时有值
    pathType?: Number;
    // 菜单地址，仅menuType=2时有值
    path?: string;
    // 图标类型（1-ElementPlus图标 2-iconify），menuType=1/2时有值
    iconType?: number;
    // 图标值，menuType=1/2时有值
    icon?: string;
    // 权限字符集合，仅menuType=2时有值
    perms?: string[];
    // 祖先菜单数组，一级菜单时值为空数组
    ancestors: MenuItem[];
    // 子菜单数组，无子菜单时可为空
    children?: MenuItem[];
    // 菜单打开后是否可关闭
    closable?: boolean;

    constructor(menuId: string, menuName: string, menuType: Number, pathType?: Number, path?: string, iconType?: number, icon?: string, perms: string[] = [], ancestors: MenuItem[] = [], children?: MenuItem[], closable?: boolean) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuType = menuType;
        this.pathType = pathType;
        this.path = path;
        this.iconType = iconType;
        this.icon = icon;
        this.perms = perms;
        this.ancestors = ancestors;
        this.children = children;
        this.closable = closable;
    }

    /**
     * 响应记录中的菜单数据转换为菜单对象数组
     * @param menuRecords 响应记录数组
     * @param parentMenu 父级菜单对象
     * @returns 菜单对象数组
     */
    static fromMenuRecords(menuRecords: JSON[], parentMenu: MenuItem): MenuItem[] {
        let menuArray = [];
        menuRecords.forEach((record) => {
            let ancestors = [];
            if (parentMenu) {
                ancestors.push(parentMenu);
                if (parentMenu.ancestors) {
                    ancestors = ancestors.concat(parentMenu.ancestors);
                }
            }
            let menu: MenuItem = {
                menuId: record["menuId"].toString(),
                menuName: record["menuName"],
                menuType: record["menuType"],
                iconType: record["iconType"],
                icon: record["icon"],
                pathType: record["pathType"],
                path: record["path"],
                perms: record["perms"] || [],
                ancestors: ancestors,
                closable: record["closable"] == undefined ? true : record["closable"],
            };
            if (record["children"]) {
                let childrenMenus = MenuItem.fromMenuRecords(record["children"], menu);
                if (childrenMenus) {
                    menu.children = childrenMenus;
                }
            }
            // pathType-1:内部组件地址  pathType-2:为外部链接地址
            if (menu.pathType === 2) {
                menu.path = '/index/ref/' + encodeURIComponent(menu.path);
            }
            menuArray.push(menu);
        });
        return menuArray;
    }

    /**
     * 通过地址查找菜单
     * @param path 地址
     * @param menuItems 菜单对象数组
     * @returns 菜单对象
     */
    static findMenuItemByPath(path: string, menuItems: MenuItem[]): MenuItem | null {
        for (let i = 0; i < menuItems.length; i++) {
            let menuItem = menuItems[i];
            if (menuItem.path === path) {
                return menuItem;
            }
            if (menuItem.children && menuItem.children.length > 0) {
                let childMenuItem = MenuItem.findMenuItemByPath(path, menuItem.children);
                if (childMenuItem) {
                    return childMenuItem;
                }
            }
        }
        return null;
    }
}
