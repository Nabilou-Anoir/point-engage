<template>
  <div class="main-layout">
    <!-- Main Content -->
    <main class="main-content" style="margin-left: 40px;">
      <!-- ✅ Nouveau titre principal en violet -->
      <h1 style="color: #6a3fa0; font-size: 28px; margin-bottom: 8px;">
        Service Scolarité
      </h1>

      <!-- ✅ Sous-titre -->
      <h2 class="title">Liste des étudiants envoyée</h2>

      <div class="table-section">
        <!-- ✅ Bouton Export en haut à droite -->
        <div
          v-if="isSent && eleves.length"
          style="display: flex; justify-content: flex-end; margin-bottom: 12px"
        >
          <button
            @click="exportToCSV"
            style="padding: 8px 16px; background-color: #6a3fa0; color: white; border: none; border-radius: 6px; cursor: pointer;"
          >
            Exporter en CSV
          </button>
        </div>

        <!-- ✅ Étend le tableau sur toute la largeur -->
        <div class="table-container" style="width: 100%;">
          <table class="styled-table" v-if="isSent && eleves.length" style="width: 100%;">
            <thead>
              <tr>
                <th>Nom</th>
                <th>Promotion</th>
                <th class="align-right">Points accordés</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(eleve, index) in eleves" :key="index">
                <td>{{ eleve.name }}</td>
                <td>{{ eleve.promotion }}</td>
                <td class="align-right">
                  <span>{{ eleve.points }}</span>
                </td>
              </tr>
            </tbody>
          </table>

          <!-- Message si aucune liste n'a été envoyée -->
          <div v-else>
            <p>Aucune liste n'a encore été envoyée.</p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

const eleves = ref([]);
const isSent = ref(false);

onMounted(() => {
  const sentFlag = sessionStorage.getItem("isSent");
  const storedData = sessionStorage.getItem("elevesFiltres");

  if (sentFlag === "true" && storedData) {
    isSent.value = true;
    try {
      const parsed = JSON.parse(storedData);
      eleves.value = parsed.map(eleve => ({
        name: eleve.name,
        promotion: eleve.promotion,
        points: eleve.points
      }));
    } catch (e) {
      console.error("Erreur de parsing des données envoyées:", e);
    }
  } else {
    isSent.value = false;
  }
});

function exportToCSV() {
  const headers = ["Nom", "Promotion", "Points accordés"];
  const rows = eleves.value.map(e => [e.name, e.promotion, e.points]);

  const csvContent = [headers, ...rows].map(e => e.join(";")).join("\n");

  const BOM = "\uFEFF";
  const blob = new Blob([BOM + csvContent], { type: "text/csv;charset=utf-8;" });

  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.setAttribute("download", "etudiants.csv");
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}
</script>

<style scoped src="./ServiceScolarite.css"></style>
