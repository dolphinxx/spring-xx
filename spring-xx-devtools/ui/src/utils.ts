export const nextId = () => "" + new Date().getTime() + String(Math.trunc(Math.random() * 1000)).padEnd(3, "0");
