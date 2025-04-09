<template>
  <div class="accueil-etudiant">
    <div class="header">
      <h1 v-if="studentLoaded">Bonjour, {{ student.prenom }} {{ student.nom }}</h1>
      <h1 v-else>Bonjour !</h1>
      <p class="subtitle">Voici un aperçu de vos activités et participations.</p>
    </div>

    <div class="stats" v-if="studentLoaded">
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-chart-line" style="color: #007074;"></i>
        </div>
        <div class="stat-content">
          <span class="stat-label">Total de points obtenus</span>
          <span class="stat-value" style="color: #007074;">{{ totalPointsValides }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-check-circle" style="color: #27AE60;"></i>
        </div>
        <div class="stat-content">
          <span class="stat-label">Nombre de participations</span>
          <span class="stat-value" style="color: #27AE60;">{{ nombreParticipations }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-hourglass-half" style="color: #143D60;"></i>
        </div>
        <div class="stat-content">
          <span class="stat-label">Fiches en cours de validation</span>
          <span class="stat-value" style="color: #143D60;">{{ nbFichesEnCours }}</span>
        </div>
      </div>
    </div>

    <div v-if="semestreDepot" class="stats">
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-calendar-alt" style="color: #8E44AD;"></i>
        </div>
        <div class="stat-content">
          <span class="stat-label">Début du dépôt de fiche</span>
          <span class="stat-value">{{ formatDate(semestreDepot.dateDebutDepot) }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-calendar-times" style="color: #C0392B;"></i>
        </div>
        <div class="stat-content">
          <span class="stat-label">Fin du dépôt de fiche</span>
          <span class="stat-value">{{ formatDate(semestreDepot.dateFinDepot) }}</span>
        </div>
      </div>
    </div>

    <div class="info">
      <p>Pour plus d’informations, cliquez sur le bouton ci-dessous.</p>
      <button @click="navigateToDispositif" class="modern-button">Cliquez ici !</button>
    </div>

    <div class="privacy-message">
      <p>Vos données seront protégées et supprimées de nos serveurs dès que vous quitterez l'établissement.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

const student = ref(null);
const totalPointsValides = ref(0);
const nombreParticipations = ref(0);
const nbFichesEnCours = ref(0);
const studentLoaded = ref(false);
const semestreDepot = ref(null);

const STUDENT_API_URL = "http://localhost:8989/api/etudiants/byEmail";
const PARTICIPATIONS_API_URL = "http://localhost:8989/api/participes/search/findByIdIdEtudiant";
const SEMESTRE_API_URL = "http://localhost:8989/api/semestres";

const router = useRouter();

const formatDate = (dateStr) => {
  if (!dateStr) return "-";
  const d = new Date(dateStr);
  return d.toLocaleDateString("fr-FR");
};

onMounted(async () => {
  const loggedInUser = sessionStorage.getItem("loggedInUser");
  if (loggedInUser) {
    const user = JSON.parse(loggedInUser);
    try {
      const studentResponse = await fetch(`${STUDENT_API_URL}?email=${encodeURIComponent(user.email)}`);
      const studentData = await studentResponse.json();
      student.value = studentData;

      const partRes = await fetch(`${PARTICIPATIONS_API_URL}?idEtudiant=${studentData.idEtudiant}`);
      const partData = await partRes.json();
      const participations = partData._embedded?.participes || [];

      let sumPoints = 0, countEnCours = 0;
      for (const p of participations) {
        if (p.statut === true && p.totalPoints) sumPoints += p.totalPoints;
        if (p.statut === null) countEnCours++;
      }

      totalPointsValides.value = sumPoints;
      nombreParticipations.value = participations.length;
      nbFichesEnCours.value = countEnCours;
      studentLoaded.value = true;

      const semestresRes = await fetch(SEMESTRE_API_URL);
      if (semestresRes.ok) {
        const semestres = await semestresRes.json();
        const list = semestres._embedded?.semestres || semestres;
        if (list.length) {
          list.sort((a, b) => new Date(b.dateDebutSemestre) - new Date(a.dateDebutSemestre));
          semestreDepot.value = list[0];
        }
      }
    } catch (e) {
      console.error("Erreur lors du chargement :", e);
    }
  }
});

const navigateToDispositif = () => {
  router.push('/etudiant/dispositif');
};
</script>

<style scoped >
.accueil-etudiant {
  padding: 40px 20px;
  font-family: 'Inter', sans-serif;
  max-width: 1200px;
  margin: 0 auto;
  background-color: #fafafa;
  min-height: 100vh;
}

/* En-tête */
.header {
  text-align: center;
  margin-bottom: 60px;
}

.header h1 {
  font-size: 2.8rem;
  color: #2c3e50;
  margin-bottom: 10px;
  font-weight: 700;
}

.header .subtitle {
  font-size: 1.1rem;
  color: #7f8c8d;
  font-weight: 400;
}

/* Statistiques */
.stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 60px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  display: flex;
  align-items: center;
  border: 1px solid #e0e0e0;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 1.5rem;
  margin-right: 20px;
  color: #6A3FA0;
}

.stat-content {
  flex: 1;
}

.stat-label {
  display: block;
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 5px;
  font-weight: 500;
}

.stat-value {
  font-size: 1.8rem;
  font-weight: 700;
  color: #333;
}

/* Informations supplémentaires */
.info {
  text-align: center;
  margin-bottom: 60px;
}

.info p {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 20px;
  font-weight: 400;
}

.modern-button {
  background: linear-gradient(135deg, #6A3FA0, #8E44AD);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 25px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 600;
  transition: background 0.3s ease, transform 0.3s ease;
}

.modern-button:hover {
  background: linear-gradient(135deg, #8E44AD, #6A3FA0);
  transform: scale(1.05);
}

/* Message de confidentialité */
.privacy-message {
  text-align: center;
  font-size: 0.9rem;
  color: #7f8c8d;
  margin-top: 60px;
  font-weight: 400;
}
</style>
