import axios from "axios";

const API_URL = "/api";

export default {
  login(username, password) {
    // Envoie une requête POST au backend
    return axios.post(`${API_URL}/login`, { username, password });
  },
};
