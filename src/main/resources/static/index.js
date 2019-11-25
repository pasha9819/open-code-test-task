new Vue({
    el: "#page",
    data: {
        mode: "menu",
        current: "",
        serverError: false,
        steps: [],
        currentHistoryPage: 0,
        currentRatingPage: 0,
        historyPageCount: 1,
        historyRatingCount: 1,
        logs: [],
        gameLog: [],
        rating: []
    },
    methods:{
        clear() {
            this.current = '';
        },
        append(number) {
            if (this.current.length === 0 && number === '0') {
                return;
            }
            if (this.current.length === 4){
                alert("Число должно состоять ровно из 4 цифр");
                return;
            }
            if (this.current.indexOf(`${number}`) !== -1){
                alert("Цифры не должны повторяться");
                return;
            }
            this.current = `${this.current}${number}`;
        },
        send() {
            if(this.current.length === 0){
                alert("Введите число!");
                return;
            }
            this.$resource(ctx + 'game/step').get({userNumber : this.current})
                .then(
                    response => {
                        if (response.body.status === "ok"){
                            this.steps.push(response.body.step);
                            this.current = "";
                        }
                        else if (response.body.status === "error"){
                            alert(response.body.error_message);
                        }
                        else if (response.body.status === "win"){
                            this.mode = "win";
                        }else {
                            window.location = ctx
                        }
                    },
                    () => {
                        this.mode = "error";
                    });
        },
        toMenu(){
            this.current = "";
            this.mode = "menu";
            this.currentHistoryPage = 0;
            this.currentRatingPage = 0;
        },
        startGame(){
            this.$resource(ctx + 'game/start').get()
                .then(
                    response => {
                        if (response.body.game === "old"){
                            this.steps = response.body.steps;
                            this.mode = "game";
                        }
                        else if (response.body.game === "new"){
                            this.steps = [];
                            this.mode = "game";
                        }else {
                            window.location = ctx
                        }
                    },
                    () => {
                        this.mode = "error";
                    });
        },
        loadHistory(){
            this.$resource(ctx + 'history').get({page: this.currentHistoryPage})
                .then(
                    response => {
                        if (response.body.status === "ok"){
                            this.logs = response.body.logs;
                            this.historyPageCount = response.body.historyPageCount;
                            this.mode = "history";
                        }
                        else if (response.body.status === "error"){
                            alert(response.body.error_message);
                        }else {
                            window.location = ctx
                        }
                    },
                    () => {
                        this.mode = "error";
                    });
        },
        prevHistory(){
            this.currentHistoryPage--;
            this.loadHistory();
        },
        nextHistory(){
            this.currentHistoryPage++;
            this.loadHistory();
        },
        toAttemptHistory(attempt){
            console.log(attempt);
            this.$resource(ctx + 'history/' + attempt).get()
                .then(
                    response => {
                        if (response.body.status === "ok"){
                            this.gameLog = response.body.gameLog;
                            this.mode = "attemptHistory";
                        }
                        else if (response.body.status === "error"){
                            alert(response.body.error_message);
                        }else {
                            window.location = ctx
                        }
                    },
                    () => {
                        this.mode = "error";
                    });
        },
        loadRating(){
            this.$resource(ctx + 'rating').get({page: this.currentRatingPage})
                .then(
                    response => {
                        if (response.body.status === "ok"){
                            this.rating = response.body.rating;
                            this.ratingPageCount = response.body.ratingPageCount;
                            this.mode = "rating";
                        }
                        else if (response.body.status === "error"){
                            alert(response.body.error_message);
                        }else {
                            window.location = ctx
                        }
                    },
                    () => {
                        this.mode = "error";
                    });
        },
        prevRating(){
            this.currentRatingPage--;
            this.loadRating();
        },
        nextRating(){
            this.currentRatingPage++;
            this.loadRating();
        },
    }
});