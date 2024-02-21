async function delay(ms) {
  return new Promise((resolve, reject) => {
    setTimeout(() => resolve(), ms);
  });
}

function restSuccess(data) {
  return {
    status: 200,
    data,
    msg: null,
  };
}

function restServerError(msg) {
  return {
    status: 500,
    msg,
    data: null,
  };
}

function extFromMimeType(mimeType) {
  // TODO:
  return null;
}

function offsetDay(date, offset) {
  date.setDate(date.getDate() + offset);
  return date;
}

module.exports = {
  delay,
  restSuccess,
  restServerError,
  extFromMimeType,
  offsetDay,
}
