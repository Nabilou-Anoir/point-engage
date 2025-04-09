<template>
  <div class="main-content">
    <h1 class="title">Accueil Référent</h1>

    <!-- 🎯 Filtres -->
    <div class="filters">
      <div class="filter-group">
        <label>Date début</label>
        <input type="date" class="date-input" v-model="dateDebut" />
      </div>
      <div class="filter-group">
        <label>Date fin</label>
        <input type="date" class="date-input" v-model="dateFin" />
      </div>
      <div class="filter-group">
        <label>Promotion</label>
        <select v-model="selectedPromotion" class="select-input">
          <option value="">Toutes</option>
          <option v-for="promo in availablePromotions" :key="promo">{{ promo }}</option>
        </select>
      </div>
    </div>

    <!-- 📋 Tableau -->
    <div class="table-container">
      <table class="styled-table">
        <thead>
        <tr>
          <th>Nom</th>
          <th>Promotion</th>
          <th>Engagement</th>
          <th>Résumé</th>
          <th>Validé</th>
          <th>Points envisagés</th>
          <th>Remarque référent</th>
          <th>Points accordés</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="student in filteredStudents" :key="student.etudiantId">
          <td @click="openStudentModal(student)" style="cursor: pointer; color: blue">
            {{ student.name }}
          </td>
          <td>{{ student.promotion }}</td>
          <td>{{ student.engagementType }}</td>
          <td>
            <input type="text" v-model="student.resumeDirecteur" class="editable-input" />
          </td>
          <td>
            <label class="custom-checkbox">
              <input
                type="checkbox"
                :disabled="student.valide"
                @change="handleValidation(student)"
                class="hidden-checkbox"
              />
              <span class="checkmark"></span>
            </label>
          </td>
          <td>
            <input
              type="number"
              v-model.number="student.nbPointsEnvisages"
              min="0"
              max="0.5"
              step="0.01"
              class="editable-input"
              @input="limiterValeur(student, 'nbPointsEnvisages')"
            />
          </td>
          <td>{{ student.remarqueReferent }}</td>
          <td>
            <input
              type="number"
              v-model.number="student.pointsAccordes"
              min="0"
              max="0.5"
              step="0.01"
              class="editable-input"
              @input="limiterValeur(student, 'pointsAccordes')"
            />
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- ✅ Bouton -->
    <div class="button-container">
      <button class="btn-submit" @click="validerEnvoi">
        Enregistrer
      </button>
    </div>

    <!-- ✅ Confirmation -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal">
        <h3>Données enregistrées</h3>
        <button class="modal-btn" @click="showModal = false">Fermer</button>
      </div>
    </div>

    <!-- 🔥 Modale d'infos étudiant -->
    <div v-if="isModalOpen" class="modal-overlay">
      <div class="modal">
        <span class="modal-close" @click="closeModal">&times;</span>
        <h3>Détails de {{ selectedStudent.name }}</h3>
        <p><strong>Promotion :</strong> {{ selectedStudent.promotion }}</p>
        <p><strong>Engagement :</strong> {{ selectedStudent.engagementType }}</p>
        <p><strong>Action :</strong> {{ selectedStudent.actionType }}</p>
        <p><strong>Description :</strong> {{ selectedStudent.description }}</p>
        <button class="modal-btn" @click="closeModal">Fermer</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      students: [],
      selectedPromotion: "",
      availablePromotions: [],
      dateDebut: "",
      dateFin: "",
      showModal: false,
      isModalOpen: false,
      selectedStudent: null,
      baseURL: "http://localhost:8989/api",
    };
  },
  computed: {
    filteredStudents() {
      return this.students.filter((s) =>
        this.selectedPromotion ? s.promotion === this.selectedPromotion : true
      );
    },
  },
  mounted() {
    const cached = sessionStorage.getItem("participationsAttribuées");
    if (cached) {
      this.students = JSON.parse(cached);
      this.availablePromotions = [...new Set(this.students.map(s => s.promotion))];
    } else {
      this.loadData();
    }
  },
  watch: {
    $route() {
      this.loadData();
    },
  },
  methods: {
    async loadData() {
      try {
        const { data: participations } = await axios.get(`${this.baseURL}/participes`);
        const filtered = participations.filter(p => p.pointAction === null);

        const studentList = await Promise.all(filtered.map(async (p) => {
          try {
            const [etudiant, action, semestre, referentiel] = await Promise.all([
              axios.get(`${this.baseURL}/etudiants/${p.id.idEtudiant}`),
              axios.get(`${this.baseURL}/actions/${p.id.idAction}`),
              axios.get(`${this.baseURL}/semestres/${p.id.idSemestre}`),
              axios.get(`${this.baseURL}/referentiels/${p.idReferentiel || 1}`),
            ]);

            return {
              name: `${etudiant.data.prenom} ${etudiant.data.nom}`,
              promotion: etudiant.data.promotion,
              engagementType: referentiel.data.nom || "N/A",
              actionType: action.data.nom,
              description: p.descriptionParticipation,
              resumeDirecteur: p.resumeDirecteur || "",
              remarqueReferent: p.remarqueReferent || "",
              nbPointsEnvisages: p.nbPointsAttribue || 0,
              pointsAccordes: p.pointAction || 0,
              valide: p.statut ?? false,
              etudiantId: etudiant.data.idEtudiant,
              actionId: action.data.idAction,
              semestreId: semestre.data.idSemestre,
            };
          } catch (e) {
            console.error("Erreur sur une participation :", e);
            return null;
          }
        }));

        this.students = studentList.filter(Boolean);
        this.availablePromotions = [...new Set(this.students.map(s => s.promotion))];
      } catch (error) {
        console.error("Erreur de chargement des participations :", error);
      }
    },

    limiterValeur(student, champ) {
      student[champ] = Math.min(Math.max(0, parseFloat(student[champ] || 0)), 0.5).toFixed(2);
    },

    handleValidation(student) {
      if (!student.valide) student.valide = true;
    },

    async validerEnvoi() {
      try {
        const results = await Promise.allSettled(
          this.students.map((student) =>
            axios.put(`${this.baseURL}/participes/${student.etudiantId}/${student.actionId}/${student.semestreId}`, {
              statut: student.valide,
              nbPointsAttribue: student.nbPointsEnvisages,
              pointAction: student.pointsAccordes,
              resumeDirecteur: student.resumeDirecteur,
            })
          )
        );

        const hasErrors = results.some((res) => res.status === "rejected");
        if (!hasErrors) {
          sessionStorage.setItem("participationsAttribuées", JSON.stringify(this.students));
          this.showModal = true;
        } else {
          alert("Certaines données n'ont pas été envoyées.");
        }
      } catch (e) {
        console.error("Erreur d’envoi :", e);
        alert("Erreur lors de l’envoi.");
      }
    },

    openStudentModal(student) {
      this.selectedStudent = student;
      this.isModalOpen = true;
    },

    closeModal() {
      this.isModalOpen = false;
    },
  },
};
</script>

<style scoped src="./AttribuerPointsView.css"></style>
