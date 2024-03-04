const demoMenus:Btn[] = import.meta.env.DEV ? [
  {
    id: 10000,
    name: 'Demo',
    key: 'demo',
    type: 1,
    parentId: null,
    order: 10000,
    target: null,
    icon: 'mdi-apps',
    children: [
      {
        id: 10001,
        name: 'MenusDemo',
        key: 'menu-demo',
        type: 1,
        parentId: null,
        order: 1,
        icon: 'mdi-menu',
        target: '/demo/menu_demo',
      },
      {
        id: 10002,
        name: 'AutoFormDemo',
        key: 'auto-form-demo',
        type: 1,
        parentId: null,
        order: 2,
        icon: 'mdi-menu',
        target: '/demo/auto_form_demo',
      },
      {
        id: 10003,
        name: 'CrudTableDemo',
        key: 'crud-table-demo',
        type: 1,
        parentId: null,
        order: 3,
        icon: 'mdi-menu',
        target: '/demo/crud_table_demo',
      },
    ]
  },
] : [];
export default demoMenus;
