<template>
  <div class="referent-view">
    <!-- Barre de recherche et filtres -->
    <div class="search-and-filter">
      <input type="text" v-model="searchQuery" placeholder="Rechercher un élève..." class="search-input" />
      <select v-model="selectedFilter" class="filter-select">
        <option value="">Tous les types d'engagement</option>
        <option v-for="ref in referents" :key="ref.idReferent" :value="ref.nom">{{ ref.nom }}</option>
      </select>
      <select v-model="selectedPromotion" class="filter-select">
        <option value="">Toutes les promotions</option>
        <option v-for="promo in availablePromotions" :key="promo">{{ promo }}</option>
      </select>
    </div>

    <!-- Tableau -->
    <div class="table-container">
      <table class="students-table">
        <thead>
          <tr>
            <th>Nom</th>
            <th>Promotion</th>
            <th>Type d'engagement</th>
            <th>Résumé</th>
            <th>Points envisagés</th>
            <th>Remarque</th>
            <th>Points accordés</th>
            <th>Validé</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="student in paginatedStudents" :key="student.etudiantId">
            <td @click="openStudentModal(student)" style="cursor: pointer; color: blue">{{ student.name }}</td>
            <td>{{ student.promotion }}</td>
            <td>{{ student.engagementType }}</td>
            <td><input type="text" v-model="student.resumeDirecteur" /></td>
            <td><input type="number" v-model.number="student.nbPointsEnvisages" min="0" max="0.5" step="0.01" @change="limiterValeur(student, 'nbPointsEnvisages')" /></td>
            <td><input type="text" v-model="student.remarqueReferent" /></td>
            <td><input type="number" v-model.number="student.pointsAccordes" min="0" max="0.5" step="0.01" @change="limiterValeur(student, 'pointsAccordes')" /></td>
            <td>
              <input type="checkbox" :checked="student.valide" @change="handleValidation(student)" :disabled="student.valide" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Bouton d'envoi -->
    <div class="button-container">
      <button class="btn-submit" @click="validerEnvoi">Envoyer au service de scolarité</button>
    </div>

    <!-- Modale Détails -->
    <div v-if="isModalOpen" class="modal-overlay">
      <div class="modal-content">
        <span class="close-modal" @click="closeModal">&times;</span>
        <div class="modal-header">
          <h2>{{ selectedStudent.name }}</h2>
        </div>
        <div class="modal-body">
          <p><strong>Promotion:</strong> {{ selectedStudent.promotion }}</p>
          <p><strong>Engagement:</strong> {{ selectedStudent.engagementType }}</p>
          <p><strong>Action:</strong> {{ selectedStudent.actionType }}</p>
          <p><strong>Description:</strong> {{ selectedStudent.description }}</p>
        </div>
        <div class="modal-footer">
          <button @click="closeModal">Fermer</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      students: [],
      searchQuery: '',
      selectedFilter: '',
      selectedPromotion: '',
      availablePromotions: [],
      currentPage: 1,
      itemsPerPage: 5,
      isModalOpen: false,
      selectedStudent: null,
      referents: [],
    };
  },
  computed: {
    filteredStudents() {
      return this.students.filter((student) => {
        const matchesSearch = student.name.toLowerCase().includes(this.searchQuery.toLowerCase());
        const matchesFilter = this.selectedFilter ? student.engagementType === this.selectedFilter : true;
        const matchesPromotion = this.selectedPromotion ? student.promotion === this.selectedPromotion : true;
        return matchesSearch && matchesFilter && matchesPromotion;
      });
    },
    paginatedStudents() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredStudents.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.filteredStudents.length / this.itemsPerPage);
    },
  },
  async mounted() {
    try {
      const participationsRes = await axios.get('http://localhost:8989/api/participes');
      const participations = participationsRes.data.filter(p => p.statut === false || p.statut === null);

      const studentList = await Promise.all(participations.map(async (p) => {
        try {
          const [etudiant, action, semestre, referentiel] = await Promise.all([
            axios.get(`http://localhost:8989/api/etudiants/${p.id.idEtudiant}`),
            axios.get(`http://localhost:8989/api/actions/${p.id.idAction}`),
            axios.get(`http://localhost:8989/api/semestres/${p.id.idSemestre}`),
            axios.get(`http://localhost:8989/api/referentiels/${p.idReferentiel || 1}`)
          ]);

          return {
            name: etudiant.data.prenom + ' ' + etudiant.data.nom,
            promotion: etudiant.data.promotion,
            engagementType: referentiel.data.nom || 'Non défini',
            actionType: action.data.nom,
            summary: p.resumeDirecteur,
            description: p.descriptionParticipation,
            resumeDirecteur: p.resumeDirecteur || '',
            remarqueReferent: p.remarqueReferent || '',
            nbPointsEnvisages: p.nbPointsAttribue || 0,
            pointsAccordes: p.pointAction || 0,
            valide: p.statut ?? false,
            etudiantId: etudiant.data.idEtudiant,
            actionId: action.data.idAction,
            semestreId: semestre.data.idSemestre,
          };
        } catch (e) {
          console.error('Erreur sur une participation :', e);
          return null;
        }
      }));

      this.students = studentList.filter(Boolean);
      this.availablePromotions = [...new Set(this.students.map(s => s.promotion))];
    } catch (error) {
      console.error('Erreur de chargement des données :', error);
    }
  },
  methods: {
    openStudentModal(student) {
      this.selectedStudent = student;
      this.isModalOpen = true;
    },
    closeModal() {
      this.isModalOpen = false;
    },
    handleValidation(student) {
      if (!student.valide) {
        student.valide = true;
      }
    },
    limiterValeur(student, champ) {
      if (student[champ] < 0) student[champ] = 0;
      if (student[champ] > 0.5) student[champ] = 0.5;
    },
    validerEnvoi() {
      this.students.forEach(async (student) => {
        const compositeId = `${student.etudiantId}-${student.actionId}-${student.semestreId}`;
        try {
          await axios.patch(`http://localhost:8989/api/participes/${compositeId}`, {
            statut: student.valide,
            nbPointsAttribue: student.nbPointsEnvisages,
            pointAction: student.pointsAccordes,
            remarqueReferent: student.remarqueReferent,
            resumeDirecteur: student.resumeDirecteur,
          });
        } catch (e) {
          console.error('Erreur lors de l’envoi des points :', e);
        }
      });
      alert('Points envoyés avec succès au service de scolarité.');
    },
    previousPage() {
      if (this.currentPage > 1) this.currentPage--;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) this.currentPage++;
    },
    goToPage(page) {
      this.currentPage = page;
    },
  },
};
</script>

<style scoped src="./ReferentView.css"></style>