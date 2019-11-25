new Vue({
    el: "#page",
    data:{
        fail: false,
    },
    created: function () {
        if (window.location.search === "?error"){
            this.fail = true;
        }
    }
});