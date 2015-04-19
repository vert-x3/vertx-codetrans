module.exports = {
    invokeStringHandler: function(callback) {
        callback("callback_value");
    },
    invokeStringHandlerFirstParam: function(callback, other) {
        callback(other);
    },
    invokeStringHandlerLastParam: function(other, callback) {
        callback(other);
    },
    invokeAsyncResultHandlerSuccess: function(callback) {
        callback("hello", null);
    },
    invokeAsyncResultHandlerFailure: function(callback) {
        var future = Packages.io.vertx.core.Future.failedFuture("oh no");
        callback(null, future.cause());
    }
};