export const isPlainObject = (obj: any) => obj && obj.constructor === Object && Object.getPrototypeOf(obj) === Object.prototype;

export function cloneExplicitly(target: any, source: any, includes: string[]) {
    for (const o in source) {
        if (includes.includes(o) && Object.prototype.hasOwnProperty.call(source, o)) {
            target[o] = source[o];
        }
    }
    return target;
}

export function cloneExclusively(target: any, source: any, excludes: string[]) {
    for (const o in source) {
        if (!excludes.includes(o) && Object.prototype.hasOwnProperty.call(source, o)) {
            target[o] = source[o];
        }
    }
    return target;
}

export const cloneDefaults = (target: any, defaults: any) => {
    for (const o in defaults) {
        if (!Object.prototype.hasOwnProperty.call(target, o) && Object.prototype.hasOwnProperty.call(defaults, o)) {
            target[o] = defaults[o];
        }
    }
    return target;
}
