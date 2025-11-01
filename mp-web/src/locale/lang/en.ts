import menuManage from '@/locale/lang/menu-manage/en'
import orgManage from '@/locale/lang/org-manage/en'
import postManage from '@/locale/lang/post-manage/en'
import roleManage from '@/locale/lang/role-manage/en'
import userManage from '@/locale/lang/user-manage/en'
import dictManage from '@/locale/lang/dict-manage/en'
import paramsManage from '@/locale/lang/params-manage/en'
import logLogin from '@/locale/lang/log-login/en'
import logOperate from '@/locale/lang/log-operate/en'
import userCenter from '@/locale/lang/user-center/en'

export const en = {
    systemName: '',
    welcome: 'Welcome',
    logout: 'Logout',
    updatePassword: 'Update Password',
    homeMenuName: 'Home',
    error: {
        networkMessage: 'Network or Service error, please try again later({refMessage})',
        sessionExpired: 'login session has expired, please login again.',
    },
    componentSize: {
        large: 'Large',
        default: 'Default',
        small: 'Small',
    },
    login: {
        usernamePlaceholder: 'Input Username',
        passwordPlaceholder: 'Input Password',
        captchaPlaceholder: 'Input Captcha',
        loginBtnText: 'Login',
    },
    mfa: {
        title: 'MFA Multi-Factor Authentication',
        sendButtonText: 'Send Code',
        resendButtonText: 'Resend{remainingTime}',
        inputPlaceHolder: 'Verification Code',
    },
    theme: {
        switchTheme: 'Switch Theme',
    },
    common: {
        add: 'Add',
        edit: 'Edit',
        delete: 'Delete',
        operation: 'Operation',
        expandOrCollapse: 'Expand/Collapse',
        search: 'Search',
        reset: 'Reset',
        prompt: 'Prompt',
        open: 'Open',
        confirmDeleteDesc: 'This operation will delete the data permanently, do you want to continue?',
        openMFADesc: 'This operation will open MFA verification process, do you want to continue?',
        resetMFADesc: 'This operation will reset MFA, do you want to continue?',
        addSuccessDesc: 'Add Success',
        updateSuccessDesc: 'Update Success',
        deleteSuccessDesc: 'Delete Success',
        operateSuccess: 'Operation Success',
        confirm: 'Confirm',
        cancel: 'Cancel',
        close: 'Close',
        closeCurrent: 'Close',
        closeRight: 'Close Right',
        closeLeft: 'Close Left',
        closeOther: 'Close Other',
        nextStep: 'Next Step',
        clickCopy: 'Click to copy',
        contentCopied: 'Content Copied',
        copyFail: 'Copy Fail',
        my: 'My',
        name: 'Name',
        status: 'Status',
        enable: 'Enable',
        disable: 'Disable',
        createTime: 'Create Time',
        updateTime: 'Update Time',
        remark: 'Remark',
        rootNode: 'Root',
        parentNode: 'Parent Node',
        type: 'Type',
        beginTime: 'Begin Time',
        endTime: 'End Time',
        oldPassword: 'Old Password',
        newPassword: 'New Password',
        confirmPassword: 'Confirm Password',
    },
    commonFormValidRules: {
        required: 'Required',
        lengthRange: 'Length {min}-{max} characters',
        lengthCodeRange: 'Alphanumeric and underscore format, length {min}-{max} characters',
        validConfirmPassword: 'Confirm Password is inconsistent',
    },
    ...menuManage,
    ...orgManage,
    ...postManage,
    ...roleManage,
    ...userManage,
    ...dictManage,
    ...paramsManage,
    ...logLogin,
    ...logOperate,
    ...userCenter,
}
