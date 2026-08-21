import { Injectable } from '@angular/core';
import { BehaviorSubject, share } from 'rxjs';

export interface MenuTag {
  color: string;
  value: string;
}

export interface MenuPermissions {
  only?: string | string[];
  except?: string | string[];
}

export type MenuItemType = 'link' | 'sub' | 'extLink' | 'extTabLink' | 'heading';

export interface MenuChildrenItem {
  route: string;
  name: string;
  type: MenuItemType;
  icon?: string;
  label?: MenuTag;
  badge?: MenuTag;
  children?: MenuChildrenItem[];
  permissions?: MenuPermissions;
  minPlan?: number;
}

export interface Menu extends MenuChildrenItem {
  icon: string;
}

@Injectable({
  providedIn: 'root',
})
export class MenuService {
  private readonly menu$ = new BehaviorSubject<Menu[]>([]);

  getAll() {
    return this.menu$.asObservable();
  }

  change() {
    return this.menu$.pipe(share());
  }

  set(menu: Menu[]) {
    this.menu$.next(menu);
    return this.menu$.asObservable();
  }

  add(menu: Menu) {
    const currentMenu = [...this.menu$.value, menu];
    this.menu$.next(currentMenu);
  }

  reset() {
    this.menu$.next([]);
  }

  buildRoute(routeArr: string[]) {
    let route = '';
    routeArr.forEach(item => {
      if (item?.trim()) {
        route += '/' + item.replace(/^\/+|\/+$/g, '');
      }
    });
    return route || '/';
  }

  getItemName(routeArr: string[]) {
    return this.getLevel(routeArr)[routeArr.length - 1];
  }

  private isLeafItem(item: MenuChildrenItem) {
    return !item.children?.length;
  }

  private deepClone<T>(obj: T): T {
    return JSON.parse(JSON.stringify(obj)) as T;
  }

  private isJsonObjEqual(obj0: unknown, obj1: unknown) {
    return JSON.stringify(obj0) === JSON.stringify(obj1);
  }

  private isRouteEqual(routeArr: string[], realRouteArr: string[]) {
    const normalizedRealRoute = this.deepClone(realRouteArr).filter(route => route !== '');
    return this.isJsonObjEqual(routeArr, normalizedRealRoute);
  }

  getLevel(routeArr: string[]): string[] {
    let matchedLevel: string[] = [];

    this.menu$.value.forEach(item => {
      let unhandledLayer = [{ item, parentNamePathList: [] as string[], realRouteArr: [] as string[] }];

      while (unhandledLayer.length > 0) {
        let nextUnhandledLayer: Array<{
          item: MenuChildrenItem;
          parentNamePathList: string[];
          realRouteArr: string[];
        }> = [];

        for (const element of unhandledLayer) {
          const eachItem = element.item;
          const currentNamePathList = [...element.parentNamePathList, eachItem.name];
          const currentRealRouteArr = [...element.realRouteArr, eachItem.route];

          if (this.isRouteEqual(routeArr, currentRealRouteArr)) {
            matchedLevel = currentNamePathList;
            break;
          }

          if (!this.isLeafItem(eachItem)) {
            const wrappedChildren = (eachItem.children || []).map(child => ({
              item: child,
              parentNamePathList: currentNamePathList,
              realRouteArr: currentRealRouteArr,
            }));
            nextUnhandledLayer = nextUnhandledLayer.concat(wrappedChildren);
          }
        }

        unhandledLayer = nextUnhandledLayer;
      }
    });

    return matchedLevel;
  }

  addNamespace(menu: Menu[] | MenuChildrenItem[], namespace: string) {
    menu.forEach(menuItem => {
      const originalName = menuItem.name ?? '';
      const parentPath = namespace.replace(/^menu\./, '');

      if (originalName.startsWith('menu.')) {
        menuItem.name = originalName;
      } else if (
        originalName === parentPath ||
        (parentPath && originalName.startsWith(parentPath + '.'))
      ) {
        menuItem.name = `menu.${originalName}`;
      } else {
        menuItem.name = `${namespace}.${originalName}`;
      }

      if (menuItem.children?.length) {
        this.addNamespace(menuItem.children, menuItem.name);
      }
    });
  }
}
