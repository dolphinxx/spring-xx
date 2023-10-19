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
