<template>
  <div class="main-content">
    <h1 class="title">Attribution des points</h1>

    <!-- 📅 Filtres -->
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

    <!-- 📊 Tableau -->
    <div class="table-container">
      <table class="styled-table">
        <thead>
          <tr>
            <th>Nom</th>
            <th>Promotion</th>
            <th>Type engagement</th>
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

    <!-- 📩 Bouton d’envoi -->
    <div class="button-container">
      <button class="btn-submit" @click="validerEnvoi">
        Envoyer au service de scolarité
      </button>
    </div>

    <!-- ✅ Modale de confirmation -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal">
        <h3> Accès activé !</h3>
        <p>Le service de scolarité peut maintenant visualiser les fiches avec les points attribués.</p>
        <button class="modal-btn" @click="showModal = false">OK</button>
      </div>
    </div>

    <!-- 🔍 Modale de détails étudiant -->
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
    };
  },
  computed: {
    filteredStudents() {
      return this.students.filter((student) =>
        this.selectedPromotion ? student.promotion === this.selectedPromotion : true
      );
    },
  },
  async mounted() {
    try {
      const res = await axios.get("http://localhost:8989/api/participes");
      const participations = res.data.filter(p => p.statut === false || p.statut === null);

      const studentList = await Promise.all(
        participations.map(async (p) => {
          try {
            const [etudiant, action, semestre, referentiel] = await Promise.all([
              axios.get(`http://localhost:8989/api/etudiants/${p.id.idEtudiant}`),
              axios.get(`http://localhost:8989/api/actions/${p.id.idAction}`),
              axios.get(`http://localhost:8989/api/semestres/${p.id.idSemestre}`),
              axios.get(`http://localhost:8989/api/referentiels/${p.idReferentiel || 1}`),
            ]);

            return {
              name: etudiant.data.prenom + " " + etudiant.data.nom,
              promotion: etudiant.data.promotion,
              engagementType: referentiel.data.nom || "Non défini",
              actionType: action.data.nom,
              description: p.descriptionParticipation,
              resumeDirecteur: p.resumeDirecteur || "",
              remarqueReferent: p.remarqueReferent || "", // affichée seulement
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
        })
      );

      this.students = studentList.filter(Boolean);
      this.availablePromotions = [...new Set(this.students.map((s) => s.promotion))];
    } catch (error) {
      console.error("Erreur de chargement des données :", error);
    }
  },
  methods: {
    limiterValeur(student, champ) {
      student[champ] = Math.min(Math.max(0, parseFloat(student[champ] || 0)), 0.5).toFixed(2);
    },
    handleValidation(student) {
      if (!student.valide) {
        student.valide = true; // empêcher décochage
      }
    },
    openStudentModal(student) {
      this.selectedStudent = student;
      this.isModalOpen = true;
    },
    closeModal() {
      this.isModalOpen = false;
    },
    async validerEnvoi() {
      try {
        const results = await Promise.allSettled(
          this.students.map((student) =>
            axios.put(`http://localhost:8989/api/participes/${student.etudiantId}/${student.actionId}/${student.semestreId}`, {
              statut: student.valide,
              nbPointsAttribue: student.nbPointsEnvisages,
              pointAction: student.pointsAccordes,
              resumeDirecteur: student.resumeDirecteur,
              // remarqueReferent : non modifiable donc pas envoyé
            })
          )
        );

        const hasErrors = results.some((res) => res.status === "rejected");
        if (hasErrors) {
          console.warn("Certaines requêtes ont échoué :", results.filter(r => r.status === "rejected"));
          alert("Certaines données n'ont pas pu être envoyées.");
        } else {
          this.showModal = true;
        }
      } catch (e) {
        console.error("Erreur globale d’envoi :", e);
        alert("Erreur d’envoi.");
      }
    },
  },
};
</script>

<style scoped src="./AttribuerPointsView.css"></style>
