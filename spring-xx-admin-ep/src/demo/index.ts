const demoMenus:Btn[] = import.meta.env.DEV ? [
  {
    id: 10000,
    name: 'Demo',
    key: 'demo',
    type: 1,
    parentId: null,
    order: 10000,
    target: null,
    icon: 'ep-grid',
    children: [
      {
        id: 10005,
        name: 'ComponentsDemo',
        key: 'components-demo',
        type: 1,
        parentId: null,
        order: 5,
        icon: 'ep-menu',
        target: '/demo/components_demo',
      },
      {
        id: 10001,
        name: 'MenusDemo',
        key: 'menu-demo',
        type: 1,
        parentId: null,
        order: 1,
        icon: 'ep-menu',
        target: '/demo/menu_demo',
      },
      {
        id: 10002,
        name: 'AutoFormDemo',
        key: 'auto-form-demo',
        type: 1,
        parentId: null,
        order: 2,
        icon: 'ep-menu',
        target: '/demo/auto_form_demo',
      },
      {
        id: 10003,
        name: 'CrudViewDemo',
        key: 'crud-view-demo',
        type: 1,
        parentId: null,
        order: 3,
        icon: 'ep-menu',
        target: '/demo/crud_view_demo',
      },
      {
        id: 10004,
        name: 'CrudView2Demo',
        key: 'crud-view2-demo',
        type: 1,
        parentId: null,
        order: 4,
        icon: 'ep-menu',
        target: '/demo/crud_view2_demo',
      },
    ]
  },
] : [];
export default demoMenus;
