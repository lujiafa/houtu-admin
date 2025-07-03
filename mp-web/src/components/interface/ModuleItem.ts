import MenuItem from './MenuItem'

export default class ModuleItem {
    moduleId: string;
    moduleName: string;
    // 菜单地址，仅menuType=2时有值
    path: string;
    // 权限字符集合，仅menuType=2时有值
    perms?: string[];
    // 祖先菜单数组，一级菜单时值为空数组
    ancestors: ModuleItem[];
    // 功能是否可关闭
    closable?: boolean = true;

    constructor(moduleId: string, moduleName: string, ancestors: ModuleItem[] = [], path?: string, perms: string[] = [], closable?: boolean) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.ancestors = ancestors;
        this.path = path;
        this.perms = perms;
        this.closable = closable;
    }

    static fromMenuItem(menuItem: MenuItem): ModuleItem {
        if (!menuItem) {
            return;
        }
        let moduleItem = new ModuleItem(menuItem.menuId, menuItem.menuName, [], menuItem.path, menuItem.perms, menuItem.closable);
        if (menuItem.ancestors && menuItem.ancestors.length > 0) {
            for (let i = 0; i < menuItem.ancestors.length; i++) {
                moduleItem.ancestors.push(ModuleItem.fromMenuItem(menuItem.ancestors[i]));
            }
        }
        return moduleItem;
    }


}
