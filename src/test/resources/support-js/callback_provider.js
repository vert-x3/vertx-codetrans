module.exports = {
    callbackWithSuccess: function(callback) {
        callback("hello", null);
    },
    callbackWithFailure: function(callback) {
        var future = Packages.io.vertx.core.Future.failedFuture("oh no");
        callback(null, future.cause());
    },
    callbackWithString: function(callback) {
        callback("world");
    }
};