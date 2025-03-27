<template>
  <div class="proposition-container">
    <!-- Titre de la section -->
    <header class="header">
      <div class="header-content">
        <h1>Proposition d’une activité hors référentiel</h1>
        <p class="description">
          Proposez une nouvelle activité qui n'est pas encore incluse dans le référentiel. 
          Votre suggestion sera examinée par la direction.
        </p>
      </div>
    </header>

    <!-- Formulaire de proposition -->
    <form @submit.prevent="submitProposition">
      <div class="form-group">
        <label for="titre">Titre :</label>
        <input type="text" id="titre" v-model="proposition.titre" required>
      </div>

      <div class="form-group">
        <label for="description">Description :</label>
        <textarea
          id="description"
          v-model="proposition.description"
          rows="5"
          required
          placeholder="Décrivez l'activité proposée"
        ></textarea>
      </div>

      <div class="form-group">
        <label for="typeEngagement">Type d’engagement proposé :</label>
        <input
          type="text"
          id="typeEngagement"
          v-model="proposition.typeEngagement"
          required
          placeholder="Exemple : Engagement environnemental, social, etc."
        >
      </div>

      <div class="form-group">
        <label for="justification">Justification :</label>
        <textarea
          id="justification"
          v-model="proposition.justification"
          rows="5"
          required
          placeholder="Justifiez pourquoi cette activité devrait être ajoutée au référentiel"
        ></textarea>
      </div>

      <div class="form-actions">
        <button type="submit" class="btn-submit">ENVOYER</button>
        <button type="button" class="btn-cancel" @click="annulerProposition">ANNULER</button>
      </div>
    </form>

    <!-- Popup de confirmation -->
    <div v-if="showPopup" class="popup-overlay" @click.self="showPopup = false">
      <div class="popup-content">
        <div class="popup-icon">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
          </svg>
        </div>
        <p class="popup-message">Votre suggestion est envoyée à la direction pour traitement !</p>
        <div class="popup-actions">
          <button class="btn-close" @click="showPopup = false">Fermer</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      proposition: {
        titre: '',
        description: '',
        typeEngagement: '',
        justification: ''
      },
      showPopup: false // Contrôle l'affichage de la popup
    };
  },
  methods: {
    // Méthode pour soumettre la proposition
    submitProposition() {
      // Validation du formulaire
      if (
        this.proposition.titre &&
        this.proposition.description &&
        this.proposition.typeEngagement &&
        this.proposition.justification
      ) {
        this.showPopup = true; // Affiche la popup
        console.log('Proposition soumise:', this.proposition);
        this.resetPropositionForm(); // Réinitialise le formulaire
      } else {
        alert('Veuillez remplir tous les champs du formulaire.');
      }
    },

    // Méthode pour annuler la proposition
    annulerProposition() {
      this.resetPropositionForm();
    },

    // Méthode pour réinitialiser le formulaire
    resetPropositionForm() {
      this.proposition = {
        titre: '',
        description: '',
        typeEngagement: '',
        justification: ''
      };
    }
  }
};
</script>

<style scoped src="./ProposerActiviteView.css"></style>