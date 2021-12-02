const app = Vue.createApp({
  data() {
    return {
      cuenta: [],
      transaction: [],
      isError: false,
      number: "",
      fromdate: "",
      todate: "",
    };
  },
  created() {
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get("id");
    axios
      .get(`/api/account/${id}`)
      .then((e) => {
        this.cuenta = e.data;
        this.transaction = e.data.transaction;
        this.number = e.data.number;
        this.transaction.sort((a, b) => {
          if (a.id < b.id) {
            return -1;
          }
          if (b.id < a.id) {
            return 1;
          }
          return 0;
        });
      })
      .catch((err) => err.message);
  },
  methods: {
    logout(e) {
      e.preventDefault();
      axios.post("/api/logout").then((window.location = "/web/index.html"));
    },
    downLoad() {
      //window.location = `/api/transaction/export/pdf?number=${this.number}&from=${this.fromdate}&to=${this.todate}`
      let init = {
          "responseType": "blob"
        }
      let hear={

        headers:{
          "Content-type":"application/pdf"
        }
      }
        axios
        .post(
          `/api/transaction/export/pdf?number=${this.number}&from=${this.fromdate}&to=${this.todate}`,
          hear,init
        )
        .then(res => {
          console.log(res)
        let inicio = res.headers["content-disposition"]
        let init = decodeURI(inicio)
        let otra = document.createElement("a")
        otra.href = URL.createObjectURL(res.data)
        otra.download = init
        otra.click()
        })
        .catch((err) =>alert(err))
    },
  },
});
let cosas = app.mount("#app");
