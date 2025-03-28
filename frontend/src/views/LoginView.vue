<template>
  <div class="login-container">
    <!-- En-tête -->
    <div class="header">Application de gestion de point ingénieur engagé</div>

    <!-- Bloc principal -->
    <div class="login-box">
      <!-- Image / logo -->
      <div class="login-left">
        <img src="@/Images/logo2.jpg" alt="Logo Ingénieur Engagé" />
      </div>

      <!-- Formulaire -->
      <div class="login-right">
        <h2 class="login-title">
          {{ isLoginMode ? "Connexion à l’espace Ingénieur Engagé" : "Créer un compte Ingénieur Engagé" }}
        </h2>

        <form @submit.prevent="isLoginMode ? login() : register()">
          <input
            type="email"
            v-model="email"
            @input="detectRole"
            class="login-input"
            placeholder="Utilisez votre adresse institutionnelle."
            required
          />

          <div v-if="!isLoginMode">
            <input type="text" v-model="username" class="login-input" placeholder="Nom d'utilisateur" required />
            <input type="text" v-model="nom" class="login-input" placeholder="Nom" required />
            <input type="text" v-model="prenom" class="login-input" placeholder="Prénom" required />
            <input
              v-if="detectedRole === 'ROLE_ETUDIANT'"
              type="text"
              v-model="promotion"
              class="login-input"
              placeholder="Promotion"
              required
            />
          </div>

          <input type="password" v-model="password" class="login-input" placeholder="Mot de passe" required />

          <p class="role-detected">Rôle détecté : <strong>{{ detectedRole }}</strong></p>

          <button type="submit" class="login-button">
            {{ isLoginMode ? "Se connecter" : "S'inscrire" }}
          </button>
        </form>

        <p v-if="errorMessage" class="error-msg">⚠️ {{ errorMessage }}</p>

        <div class="login-links">
          <router-link to="#" v-if="isLoginMode">Mot de passe oublié ?</router-link>
          <span v-if="isLoginMode"> • </span>
          <button class="link-button" @click="toggleMode">
            {{ isLoginMode ? "Créer un compte" : "Déjà un compte ? Connectez-vous" }}
          </button>
        </div>
      </div>
    </div>

    <!-- Pied de page -->
    <div class="footer">
      📍 <a href="https://g.co/kgs/HXotdDv">Castres, 95 Rue Firmin Oulès, 81100 Castres</a> |
      📞 <a href="tel:0563512401">0563512401</a> |
      ✉️ <a href="mailto:isis@univ-jfc.fr">isis@univ-jfc.fr</a>
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
    const detectedRole = ref("ROLE_ETUDIANT");
    const isLoginMode = ref(true);
    const errorMessage = ref("");
    const router = useRouter();

    const detectRole = () => {
      const value = email.value.toLowerCase();
      if (value === "adrien.defossez@univ-jfc.fr") detectedRole.value = "ROLE_DIRECTEUR";
      else if (value === "scolarite-isis@univ-jfc.fr") detectedRole.value = "ROLE_SERVICE_SCOLARITE";
      else if (value.endsWith("@etud.univ-jfc.fr")) detectedRole.value = "ROLE_ETUDIANT";
      else if (value.endsWith("@univ-jfc.fr")) detectedRole.value = "ROLE_REFERENT";
      else detectedRole.value = "ROLE_ETUDIANT";
    };

    const login = async () => {
      errorMessage.value = "";
      try {
        const response = await axios.post("http://localhost:8989/auth/login", {
          email: email.value,
          password: password.value,
        });

        if (response.status === 200) {
          const user = response.data;
          sessionStorage.setItem("loggedInUser", JSON.stringify(user));
          const role = typeof user.role === "object" ? user.role.name : user.role;

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
          role: { name: detectedRole.value },
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
* {
  box-sizing: border-box;
}
body {
  padding-top: 0 !important;
  margin-top: 0 !important;
}

html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  font-family: 'Arial', sans-serif;
  background-color: #e74c3c; /* corrige la bande blanche */
}

.login-container {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  display: flex;
  flex-direction: column;
}

/* En-tête */
.header {
  flex-shrink: 0;
  width: 100%;
  background-color: #e74c3c;
  color: white;
  padding: 25px 15px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
}

/* Bloc principal */
.login-box {
  display: flex;
  flex: 1;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

/* Partie gauche */
.login-left {
  width: 50%;
  height: 100%;
  background: linear-gradient(to right, #6a1b9a, #ff7043);
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-left img {
  max-width: 70%;
  max-height: 70%;
  object-fit: contain;
}

/* Partie droite */
.login-right {
  width: 50%;
  height: 100%;
  background-color: #5F4E9B;
  padding: 30px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  overflow-y: auto;
}

.login-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: white;
}

.login-input {
  width: 100%;
  padding: 12px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 14px;
}

.login-button {
  width: 100%;
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 12px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.login-button:hover {
  background-color: #c0392b;
}

.role-detected {
  color: yellow;
  font-weight: bold;
  margin-bottom: 10px;
}

.error-msg {
  color: #ff4d4d;
  font-weight: bold;
  margin-top: 10px;
}

.login-links {
  margin-top: 10px;
  font-size: 12px;
  color: white;
}

.login-links a,
.link-button {
  color: white;
  background: none;
  border: none;
  padding: 0;
  font: inherit;
  text-decoration: underline;
  cursor: pointer;
}

/* Pied de page */
.footer {
  background-color: #fff;
  padding: 10px;
  font-size: 12px;
  color: #555;
  text-align: center;
  width: 100%;
  flex-shrink: 0;
}

/* Responsive */
@media (max-width: 768px) {
  .login-box {
    flex-direction: column;
  }

  .login-left,
  .login-right {
    width: 100%;
    height: 50%;
  }

  .login-left img {
    max-width: 60%;
  }
}
</style>
