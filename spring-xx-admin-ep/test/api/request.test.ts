import { serializeParams } from "../../src/api/request";

test('serializeParams', () => {
  const actual = serializeParams({a: 'hello', b: 'world'});
  expect(actual).toEqual('a=hello&b=world');
});
