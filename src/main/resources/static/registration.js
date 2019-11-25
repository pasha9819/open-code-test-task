new Vue({
    el: "#page",
    data:{
        serverError: false,
        username: "",
        password: "",
        passwordConfirm: "",
        usernameError: "",
        passwordError: "",
        passwordConfirmError: ""
    },
    methods:{
        reg(){
            let formData = new FormData();
            formData.append("username", this.username);
            formData.append("password", this.password);
            formData.append("passwordConfirm", this.passwordConfirm);
            formData.append(document.getElementById("csrf").getAttribute("name"),
                document.getElementById("csrf").getAttribute("value"));
            this.$resource(ctx + 'registration').save(formData)
                .then(
                    response => {
                        if (response.body.status === "ok"){
                            window.location = ctx + "login";
                        }
                        this.username = response.body.username;
                        this.password = "";
                        this.passwordConfirm = "";
                        this.usernameError = response.body.usernameError;
                        this.passwordError = response.body.passwordError;
                        this.passwordConfirmError = response.body.passwordConfirmError;
                    },
                    () => {
                        this.serverError = true;
                    });
        }
    }
});
