var bind_f = function (func_1, obj_1, params) {
    obj_1.f = func_1;
    return function () {
        try {
            obj_1.f();
        } catch (e) {
            alert(e);
        }
    }
};

