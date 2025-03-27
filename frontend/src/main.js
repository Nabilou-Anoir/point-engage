import { createApp } from "vue";
import App from "./App.vue";
import router from "./router1/index.js"; // ✅ Vérifie bien ce chemin !

const app = createApp(App);
app.use(router); // ✅ Associe Vue Router à Vue
app.mount("#app"); // ✅ Monte l'application
