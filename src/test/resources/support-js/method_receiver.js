module.exports = {
    isRed: function() {
        return true;
  }, blue: function() {
        return false;
  }, parameterizedMethodMatchingTypeVariableParameter: function(arg) {
        if (arg !== null) {
          return arg;
        } else {
          return "hello";
        }
  }, parameterizedMethodMatchingGenericParameter: function(arg) {
    if (arg !== null) {
      return arg.get();
    } else {
      return "hello";
    }
  }
};
