<template>
  <div class="referent-view">
    <!-- Barre de recherche et filtre -->
    <div class="search-and-filter">
      <!-- Barre de recherche -->
      <input
        type="text"
        v-model="searchQuery"
        placeholder="Rechercher un élève..."
        class="search-input"
      />

      <!-- Filtre par type d'engagement -->
      <select v-model="selectedFilter" class="filter-select">
        <option value="">Tous les types d'engagement</option>
        <option value="Climat-Environnement">Climat-Environnement</option>
        <option value="Ecole">Ecole</option>
        <option value="Diversité">Diversité</option>
      </select>

      <!-- Filtre par promotion -->
      <select v-model="selectedPromotion" class="filter-select">
        <option value="">Toutes les promotions</option>
        <option v-for="promotion in availablePromotions" :key="promotion" :value="promotion">
          {{ promotion }}
        </option>
      </select>
    </div>

    <!-- Conteneur du tableau avec bordures arrondies -->
    <div class="table-container">
      <table class="students-table">
        <thead>
          <tr>
            <th>Nom</th>
            <th>Promotion</th>
            <th>Type d'engagement</th>
            <th>Résumé</th>
            <th>Envoie au référent</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(student, index) in paginatedStudents" :key="index">
            <td @click="openStudentModal(student)">
              {{ student.name }}
              <!-- Icône de message devant le nom -->
              <i class="fas fa-envelope message-icon" @click.stop="sendEmail(student)"></i>
            </td>
            <td class="promotion-cell">{{ student.promotion }}</td>
            <td class="engagement-cell">{{ student.engagementType }}</td>
            <td class="summary-cell">{{ student.summary }}</td>
            <td>
              <button @click="openChooseModal(student)">Choisir</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination moderne -->
    <div class="pagination">
      <button
        @click="previousPage"
        :disabled="currentPage === 1"
        class="pagination-button prev-next"
      >
        <i class="fas fa-chevron-left"></i> <!-- Icône pour "Précédent" -->
      </button>

      <!-- Numéros de page -->
      <div class="page-numbers">
        <button
          v-for="page in totalPages"
          :key="page"
          @click="goToPage(page)"
          :class="{ active: currentPage === page }"
          class="page-number"
        >
          {{ page }}
        </button>
      </div>

      <button
        @click="nextPage"
        :disabled="currentPage === totalPages"
        class="pagination-button prev-next"
      >
        <i class="fas fa-chevron-right"></i> <!-- Icône pour "Suivant" -->
      </button>
    </div>

    <!-- Popup pour afficher la fiche de l'étudiant -->
    <div v-if="isModalOpen" class="modal-overlay">
      <div class="modal-content">
        <span class="close-modal" @click="closeModal">&times;</span>
        <div class="modal-header">
          <h2>{{ selectedStudent.name }}</h2>
        </div>
        <div class="modal-body">
          <div class="student-info">
            <p class="center-date">2024/2025</p>
            <p class="center-bold">{{ selectedStudent.semester }}</p>
          </div>
          <div class="student-details">
            <div class="detail-item">
              <span class="detail-label">Promotion:</span>
              <span class="detail-value">{{ selectedStudent.promotion }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Type d'engagement:</span>
              <span class="detail-value">
                {{ selectedStudent.engagementType }}
                <i class="fas fa-edit edit-icon" @click="editEngagementType"></i>
              </span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Type d'action:</span>
              <span class="detail-value">
                {{ selectedStudent.actionType }}
                <i class="fas fa-edit edit-icon" @click="editActionType"></i>
              </span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Période de réalisation:</span>
              <span class="detail-value">{{ selectedStudent.period }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Description:</span>
              <span class="detail-value">{{ selectedStudent.description }}</span>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="modal-button" @click="closeModal">Fermer</button>
        </div>
      </div>
    </div>

    <!-- Nouvelle modale pour "Choisir" -->
    <div v-if="isChooseModalOpen" class="modal-overlay">
      <div class="modal-content">
        <span class="close-modal" @click="closeChooseModal">&times;</span>
        <div class="modal-header">
          <h2>Référance</h2>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>Email</label>
            <input type="email" v-model="referentEmail" placeholder="Entrez l'email du référent" />
          </div>
          <div class="form-group">
            <label>Enseignant</label>
            <input type="text" v-model="referentName" placeholder="Entrez le nom de l'enseignant" />
          </div>
        </div>
        <div class="modal-footer">
          <button class="modal-button confirm" @click="confirmReferent">Confirmer</button>
          <button class="modal-button cancel" @click="closeChooseModal">Annuler</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import { students, availablePromotions } from '../../constante/students';

export default {
  data() {
    return {
      students, // Utilisez les étudiants importés
      searchQuery: '',
      selectedFilter: '',
      selectedPromotion: '', // Nouveau filtre de promotion
      availablePromotions, // Utilisez les promotions disponibles importées
      currentPage: 1,
      itemsPerPage: 5,
      isModalOpen: false,
      isChooseModalOpen: false, // Nouvel état pour la modale "Choisir"
      selectedStudent: null,
      referentEmail: '',
      referentName: '',
    };
  },
  computed: {
    // Filtrage des élèves en fonction de la recherche, du filtre et de la promotion
    filteredStudents() {
      return this.students.filter((student) => {
        const matchesSearch = student.name
          .toLowerCase()
          .includes(this.searchQuery.toLowerCase());
        const matchesFilter = this.selectedFilter
          ? student.engagementType === this.selectedFilter
          : true;
        const matchesPromotion = this.selectedPromotion
          ? student.promotion === this.selectedPromotion
          : true;
        return matchesSearch && matchesFilter && matchesPromotion;
      });
    },
    // Pagination des élèves filtrés
    paginatedStudents() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredStudents.slice(start, end);
    },
    // Calcul du nombre total de pages
    totalPages() {
      return Math.ceil(this.filteredStudents.length / this.itemsPerPage);
    },
  },
  methods: {
    // Envoyer un e-mail à l'étudiant
    sendEmail(student) {
      if (student.email) {
        window.location.href = `mailto:${student.email}`;
      } else {
        alert("Aucune adresse e-mail trouvée pour cet étudiant.");
      }
    },
    // Ouvrir la popup avec les détails de l'étudiant
    openStudentModal(student) {
      this.selectedStudent = student;
      this.isModalOpen = true;
    },
    // Fermer la popup
    closeModal() {
      this.isModalOpen = false;
    },
    // Modifier le type d'engagement
    editEngagementType() {
      alert('Modifier le type d\'engagement');
    },
    // Modifier le type d'action
    editActionType() {
      alert('Modifier le type d\'action');
    },
    // Ouvrir la modale "Choisir"
    openChooseModal(student) {
      this.selectedStudent = student;
      this.isChooseModalOpen = true;
    },
    // Fermer la modale "Choisir"
    closeChooseModal() {
      this.isChooseModalOpen = false;
    },
    // Confirmer le choix du référent
    confirmReferent() {
      if (this.referentEmail && this.referentName) {
        console.log('Référent choisi:', this.referentEmail, this.referentName);
        alert(`Référent ${this.referentName} (${this.referentEmail}) choisi pour ${this.selectedStudent.name}.`);
        this.closeChooseModal();
      } else {
        alert('Veuillez remplir tous les champs.');
      }
    },
    // Aller à la page précédente
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    // Aller à la page suivante
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    // Aller à une page spécifique
    goToPage(page) {
      this.currentPage = page;
    },
  },
};
</script>

<style scoped src="./ReferentView.css"></style>