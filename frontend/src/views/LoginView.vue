<template>
  <div class="login-container">
    <div class="form-box">
      <h2 v-if="isLoginMode">Connexion</h2>
      <h2 v-else>Inscription</h2>

      <form @submit.prevent="isLoginMode ? login() : register()">
        <div class="input-group">
          <label for="email"> Email :</label>
          <input type="email" id="email" v-model="email" @input="detectRole" required placeholder="Entrez votre email..." />
        </div>

        <div v-if="!isLoginMode">
          <div class="input-group">
            <label for="username"> Nom d'utilisateur :</label>
            <input type="text" id="username" v-model="username" required placeholder="Choisissez un nom d'utilisateur..." />
          </div>

          <div class="input-group">
            <label for="nom">Nom :</label>
            <input type="text" id="nom" v-model="nom" required placeholder="Entrez votre nom..." />
          </div>

          <div class="input-group">
            <label for="prenom"> Prénom :</label>
            <input type="text" id="prenom" v-model="prenom" required placeholder="Entrez votre prénom..." />
          </div>

          <div class="input-group" v-if="detectedRole === 'ROLE_ETUDIANT'">
            <label for="promotion">🎓 Promotion :</label>
            <input type="text" id="promotion" v-model="promotion" required placeholder="Entrez votre promotion..." />
          </div>
        </div>

        <div class="input-group">
          <label for="password"> Mot de passe :</label>
          <input type="password" id="password" v-model="password" required placeholder="Entrez votre mot de passe..." />
        </div>

        <p class="role-detected"> Rôle détecté : <strong>{{ detectedRole }}</strong></p>

        <button class="btn primary-btn" type="submit">
          {{ isLoginMode ? "🚀 Se connecter" : "✨ S'inscrire" }}
        </button>
      </form>

      <p v-if="errorMessage" class="error-msg">⚠️ {{ errorMessage }}</p>

      <button class="btn secondary-btn" @click="toggleMode">
        {{ isLoginMode ? "🔄 Pas encore inscrit ? Créez un compte" : "🔙 Déjà un compte ? Connectez-vous" }}
      </button>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

export default {
  name: "LoginView",
  setup() {
    const email = ref("");
    const username = ref("");
    const password = ref("");
    const nom = ref("");
    const prenom = ref("");
    const promotion = ref("");
    const detectedRole = ref("ROLE_ETUDIANT"); // Valeur par défaut
    const isLoginMode = ref(true);
    const errorMessage = ref("");
    const router = useRouter();

    // Détecter le rôle en fonction de l'email saisi
    const detectRole = () => {
      if (!email.value) return;
      const emailValue = email.value.toLowerCase();
      if (emailValue === "adrien.defossez@univ-jfc.fr") {
        detectedRole.value = "ROLE_DIRECTEUR";
      } else if (emailValue === "scolarite-isis@univ-jfc.fr") {
        detectedRole.value = "ROLE_SERVICE_SCOLARITE";
      } else if (emailValue.endsWith("@etud.univ-jfc.fr")) {
        detectedRole.value = "ROLE_ETUDIANT";
      } else if (emailValue.endsWith("@univ-jfc.fr")) {
        detectedRole.value = "ROLE_REFERENT";
      } else {
        detectedRole.value = "ROLE_ETUDIANT"; // Par défaut
      }
    };

    // Fonction de connexion
    const login = async () => {
      errorMessage.value = "";
      try {
        const response = await axios.post("http://localhost:8989/auth/login", {
          email: email.value,
          password: password.value,
        });

        if (response.status === 200) {
          const user = response.data;
          // Sauvegarder l'utilisateur dans le sessionStorage
          sessionStorage.setItem("loggedInUser", JSON.stringify(user));

          // Selon la structure de l'objet user, le rôle peut être un objet ou une chaîne
          const role = typeof user.role === "object" ? user.role.name : user.role;

          // Redirection en fonction du rôle
          switch (role) {
            case "ROLE_ETUDIANT":
              router.push("/etudiant/accueil");
              break;
            case "ROLE_DIRECTEUR":
              router.push("/directeur/accueil");
              break;
            case "ROLE_REFERENT":
              router.push("/referent/accueil");
              break;
            case "ROLE_SERVICE_SCOLARITE":
              router.push("/scolarite/accueil");
              break;
            default:
              router.push("/login");
          }
        }
      } catch (error) {
        errorMessage.value = "Identifiants invalides ou problème serveur.";
      }
    };

    // Fonction d'inscription (non modifiée)
    const register = async () => {
      errorMessage.value = "";
      try {
        await axios.post("http://localhost:8989/auth/register", {
          email: email.value,
          username: username.value,
          password: password.value,
          nom: nom.value,
          prenom: prenom.value,
          promotion: detectedRole.value === "ROLE_ETUDIANT" ? promotion.value : null,
          role: { name: detectedRole.value }
        });

        alert("Inscription réussie ! Connectez-vous maintenant.");
        isLoginMode.value = true;
      } catch (error) {
        errorMessage.value = "Email ou nom d'utilisateur déjà utilisé, ou problème serveur.";
      }
    };

    return {
      email,
      username,
      password,
      nom,
      prenom,
      promotion,
      detectedRole,
      isLoginMode,
      errorMessage,
      login,
      register,
      detectRole,
      toggleMode: () => (isLoginMode.value = !isLoginMode.value),
    };
  },
};
</script>

<style scoped>
/* Vos styles ici (identiques à ceux que vous utilisez) */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #6a11cb, #2575fc);
}
.form-box {
  background: rgba(255, 255, 255, 0.95);
  padding: 40px;
  width: 400px;
  border-radius: 15px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.3);
  text-align: center;
  color: #333;
}
.input-group {
  margin-bottom: 15px;
  text-align: left;
}
.input-group label {
  font-weight: bold;
  display: block;
  margin-bottom: 5px;
  color: #444;
}
.input-group input {
  width: 100%;
  padding: 12px;
  border: 2px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  color: #333;
  background: #f9f9f9;
  transition: 0.3s ease-in-out;
}
.input-group input:focus {
  border-color: #6a11cb;
  outline: none;
  background: #fff;
}
.role-detected {
  font-size: 1rem;
  margin-bottom: 10px;
  font-weight: bold;
  color: #6a11cb;
}
.btn {
  width: 100%;
  padding: 12px;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
}
.primary-btn {
  background: #4CAF50;
  color: white;
}
.secondary-btn {
  background: #ff9800;
  color: white;
}
.error-msg {
  color: #ff4d4d;
  font-weight: bold;
  margin-top: 10px;
}
</style>
