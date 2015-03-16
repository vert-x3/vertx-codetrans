module.exports = {
    create: function() {
        var obj = {
            handle : function(event) {
                Packages.io.vertx.codetrans.MethodExpressionTest.event = event;
            },
            instanceHandler : function(handler) {
                handler("hello_instance");
            }
        };
        return obj;
  }, classHandler: function(handler) {
        handler("hello_class");
    }
};