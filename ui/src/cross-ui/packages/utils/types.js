export const isString = (obj) => {
  return Object.prototype.toString.call(obj) === '[object String]';
}

export const isObject = (obj) => {
  return Object.prototype.toString.call(obj) === '[object Object]';
}

export const isNumber = (obj) => {
  return Object.prototype.toString.call(obj) === '[object Number]';
}

export const isArray = (obj) => {
  return obj instanceof Array
}

export const isHtmlElement = (node) => {
  return node && node.nodeType === Node.ELEMENT_NODE;
}

export const isFunction = (functionToCheck) => {
  const getType = {};
  return functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
}

export const isUndefined = (val)=> {
  return val === void 0;
}

export const isDefined = (val) => {
  return val !== undefined && val !== null;
}

export const isDate = (val) => {
  return val instanceof Date
}

export const isBoolean = (val) => {
  return typeof val === 'boolean'
}
