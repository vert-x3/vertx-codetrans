module.exports = {
  create: function (val) {
    return {
      get: function() {
        return val;
      }
    }
  }
};
