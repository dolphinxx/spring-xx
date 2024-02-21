let increment = 0;
export const nextId = (): string => {
    if (increment > 990) {
        increment = 0;
    }
    return new Date().getTime() + '' + (increment++).toString().padStart(3, '0');
}

export const snakeToPascalCase = (input: string): string => {
    let capitalizeNext = true;
    let result = '';
    for (let i = 0; i < input.length; i++) {
        const currentChar = input.charAt(i);
        if (currentChar == '_') {
            capitalizeNext = true;
        } else if (capitalizeNext) {
            result += currentChar.toUpperCase();
            capitalizeNext = false;
        } else {
            result += currentChar;
        }
    }
    return result;
}

export const delay = <T>(
    fn: () => T | Promise<T>,
    timeout: number
): Promise<T> => {
    return new Promise<T>((resolve, reject) => {
        setTimeout(() => {
            try {
                resolve(fn());
            } catch (error) {
                reject(error);
            }
        }, timeout);
    });
};
