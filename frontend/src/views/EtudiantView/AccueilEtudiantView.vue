<!-- src/views/EtudiantView/AccueilEtudiantView.vue -->
<template>
  <div class="layout">

    <div class="main-content">
      <!-- Contenu de la page d'accueil -->
      <div class="accueil-container">
        <h2>Votre Accueil</h2>
        <div v-if="studentLoaded" class="stats-section">
          <h3>Bonjour, {{ student.prenom }} {{ student.nom }} !</h3>
          <div class="stats-cards">
            <div class="stat-card">
              <h4>Total de points obtenus</h4>
              <p>{{ totalPointsValides }}</p>
            </div>
            <div class="stat-card">
              <h4>Nombre de participations</h4>
              <p>{{ nombreParticipations }}</p>
            </div>
            <div class="stat-card">
              <h4>Fiches en cours de validation</h4>
              <p>{{ nbFichesEnCours }}</p>
            </div>
          </div>
          <p>
            Pour plus d'info,
            <router-link to="/dispositif" class="info-link">
              cliquez ici
            </router-link>
          </p>
        </div>
        <div v-else class="stats-section">
          <p>Chargement des informations...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

const student = ref(null);
const totalPointsValides = ref(0);
const nombreParticipations = ref(0);
const nbFichesEnCours = ref(0);
const studentLoaded = ref(false);

const STUDENT_API_URL = "http://localhost:8989/api/etudiants/byEmail";
const PARTICIPATIONS_API_URL =
  "http://localhost:8989/api/participes/search/findByIdIdEtudiant";

onMounted(async () => {
  const loggedInUser = sessionStorage.getItem("loggedInUser");
  if (loggedInUser) {
    const user = JSON.parse(loggedInUser);
    console.log("🟢 Utilisateur connecté:", user);

    try {
      // Récupérer l'étudiant par e-mail
      const studentResponse = await fetch(
        `${STUDENT_API_URL}?email=${encodeURIComponent(user.email)}`
      );
      if (!studentResponse.ok) {
        throw new Error("Erreur lors du chargement de l’étudiant");
      }
      const studentData = await studentResponse.json();
      student.value = studentData;

      // Récupérer les participations
      const participationsResponse = await fetch(
        `${PARTICIPATIONS_API_URL}?idEtudiant=${student.value.idEtudiant}`
      );
      if (!participationsResponse.ok) {
        throw new Error("Erreur lors du chargement des participations");
      }
      const participationsData = await participationsResponse.json();
      const participations = participationsData._embedded?.participes || [];

      let sumPoints = 0;
      let countEnCours = 0;

      participations.forEach((item) => {
        if (item.statut === true && item.totalPoints) {
          sumPoints += item.totalPoints;
        }
        if (item.statut === null) {
          countEnCours++;
        }
      });

      totalPointsValides.value = sumPoints;
      nombreParticipations.value = participations.length;
      nbFichesEnCours.value = countEnCours;
      studentLoaded.value = true;
    } catch (error) {
      console.error(
        "Erreur lors du chargement des informations de l'étudiant :",
        error
      );
    }
  } else {
    console.error("Aucun utilisateur connecté trouvé !");
  }
});
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}

.main-content {
  flex: 1;
  padding: 20px;
}

.accueil-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.stats-section {
  margin-top: 1.5rem;
}

.stats-cards {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.stat-card {
  flex: 1;
  background-color: #f7f7f7;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  text-align: center;
}

.stat-card h4 {
  margin-bottom: 0.5rem;
  color: #6C3EA9;
}

.stat-card p {
  font-size: 1.2rem;
  font-weight: bold;
  margin: 0;
}

.info-link {
  color: #6C3EA9;
  text-decoration: underline;
  cursor: pointer;
}
</style>
