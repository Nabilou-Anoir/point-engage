<template>
  <div class="dashboard-container">
    <!-- Barre de recherche -->
    <div class="search-container">
      <input type="text" placeholder="🔍 Rechercher..." class="search-input" />
    </div>

    <!-- Indicateurs Clés (KPI) -->
    <div class="kpi-grid">
      <div class="kpi-card">
        <div class="kpi-content">
          <div class="kpi-icon">
            <i class="fas fa-users"></i>
          </div>
          <div class="kpi-text">
            <h3>Étudiants Engagés</h3>
            <p class="kpi-value">1,250</p>
            <p class="kpi-trend">+12% ce mois</p>
          </div>
        </div>
      </div>
      <div class="kpi-card">
        <div class="kpi-content">
          <div class="kpi-icon">
            <i class="fas fa-check-circle"></i>
          </div>
          <div class="kpi-text">
            <h3>Activités Validées</h3>
            <p class="kpi-value">320</p>
            <p class="kpi-trend">+8% ce mois</p>
          </div>
        </div>
      </div>
      <div class="kpi-card">
        <div class="kpi-content">
          <div class="kpi-icon">
            <i class="fas fa-chart-line"></i>
          </div>
          <div class="kpi-text">
            <h3>Taux de Participation</h3>
            <p class="kpi-value">85%</p>
            <p class="kpi-trend">+5% ce mois</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Grille du Dashboard -->
    <div class="dashboard-grid">
      <!-- 1️⃣ Graphique en barres -->
      <div class="card chart-card">
        <h2>Évolution des étudiants engagés</h2>
        <canvas ref="barChartCanvas"></canvas>
      </div>

      <!-- 2️⃣ Doughnut Chart -->
      <div class="card chart-card">
        <h2>Intérêt par promotion</h2>
        <canvas ref="doughnutChartCanvas"></canvas>
      </div>

      <!-- 3️⃣ Graphique en ligne -->
      <div class="card chart-card">
        <h2>Tendance des activités</h2>
        <canvas ref="lineChartCanvas"></canvas>
      </div>

      <!-- 4️⃣ Graphique en Aires -->
      <div class="card chart-card">
        <h2>Engagement par domaine</h2>
        <canvas ref="areaChartCanvas"></canvas>
      </div>

      <!-- 5️⃣ Graphique Radar -->
      <div class="card chart-card">
        <h2>Compétences par promotion</h2>
        <canvas ref="radarChartCanvas"></canvas>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import Chart from "chart.js/auto";

const barChartCanvas = ref(null);
const doughnutChartCanvas = ref(null);
const lineChartCanvas = ref(null);
const areaChartCanvas = ref(null);
const radarChartCanvas = ref(null);

onMounted(() => {
  // 📊 Graphique en barres (Évolution des étudiants engagés)
  if (barChartCanvas.value) {
    new Chart(barChartCanvas.value, {
      type: "bar",
      data: {
        labels: ["Jan", "Fév", "Mar", "Avr", "Mai", "Juin"],
        datasets: [
          {
            label: "Étudiants engagés",
            data: [50, 80, 65, 90, 70, 85],
            backgroundColor: createGradient(barChartCanvas.value, "#6A3FA0", "#9C88FF"),
            borderColor: "#5F4E9B",
            borderWidth: 0,
            borderRadius: 8,
          },
        ],
      },
      options: {
        responsive: true,
        animation: {
          duration: 2000,
          easing: "easeInOutQuad",
        },
        scales: {
          y: { beginAtZero: true, grid: { color: "#e0e0e0" } },
          x: { grid: { display: false } },
        },
        plugins: {
          tooltip: {
            enabled: true,
            backgroundColor: "rgba(0, 0, 0, 0.8)",
            titleFont: { size: 14 },
            bodyFont: { size: 12 },
          },
          legend: { display: false },
        },
      },
    });
  }

  
  if (doughnutChartCanvas.value) {
    new Chart(doughnutChartCanvas.value, {
      type: "doughnut",
      data: {
        labels: ["FIE1", "FIE2", "FIE3", "FIE4", "FIE5"],
        datasets: [
          {
            data: [40, 35, 25, 30, 20],
            backgroundColor: ["#6A3FA0", "#9C88FF", "#FF9F40", "#693382", "#F6DC43"],
            borderWidth: 0,
            hoverOffset: 10,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: "bottom", labels: { font: { size: 12 } } },
          tooltip: {
            enabled: true,
            backgroundColor: "rgba(0, 0, 0, 0.8)",
            titleFont: { size: 14 },
            bodyFont: { size: 12 },
          },
        },
        animation: {
          animateScale: true,
          animateRotate: true,
        },
      },
    });
  }

  // 📈 Graphique en ligne (Tendance des activités)
  if (lineChartCanvas.value) {
    new Chart(lineChartCanvas.value, {
      type: "line",
      data: {
        labels: ["Jan", "Fév", "Mar", "Avr", "Mai", "Juin"],
        datasets: [
          {
            label: "Activités validées",
            data: [30, 45, 60, 55, 80, 100],
            borderColor: "#FF9F40",
            backgroundColor: createGradient(lineChartCanvas.value, "#FF9F40", "#FF6384"),
            borderWidth: 3,
            tension: 0.4,
            fill: true,
          },
        ],
      },
      options: {
        responsive: true,
        animation: {
          duration: 1500,
          easing: "easeInOutCubic",
        },
        scales: {
          y: { beginAtZero: true, grid: { color: "#e0e0e0" } },
          x: { grid: { display: false } },
        },
        plugins: {
          tooltip: {
            enabled: true,
            backgroundColor: "rgba(0, 0, 0, 0.8)",
            titleFont: { size: 14 },
            bodyFont: { size: 12 },
          },
          legend: { display: false },
        },
      },
    });
  }

  // 📊 Graphique en Aires (Engagement par domaine)
  if (areaChartCanvas.value) {
    new Chart(areaChartCanvas.value, {
      type: "line",
      data: {
        labels: ["Tech", "Design", "Marketing", "Gestion"],
        datasets: [
          {
            label: "Engagement",
            data: [70, 50, 60, 40],
            borderColor: "#08C5D1",
            backgroundColor: createGradient(areaChartCanvas.value, "#08C5D1", "#00B4DB"),
            borderWidth: 2,
            tension: 0.4,
            fill: true,
          },
        ],
      },
      options: {
        responsive: true,
        animation: {
          duration: 1500,
          easing: "easeInOutCubic",
        },
        scales: {
          y: { beginAtZero: true, grid: { color: "#e0e0e0" } },
          x: { grid: { display: false } },
        },
        plugins: {
          tooltip: {
            enabled: true,
            backgroundColor: "rgba(0, 0, 0, 0.8)",
            titleFont: { size: 14 },
            bodyFont: { size: 12 },
          },
          legend: { display: false },
        },
      },
    });
  }

  // 📊 Graphique Radar (Compétences par promotion)
  if (radarChartCanvas.value) {
    new Chart(radarChartCanvas.value, {
      type: "radar",
      data: {
        labels: ["Tech", "Design", "Marketing", "Gestion", "Communication"],
        datasets: [
          {
            label: "FIE1",
            data: [80, 60, 70, 50, 65],
            backgroundColor: "rgba(106, 63, 160, 0.2)",
            borderColor: "#6A3FA0",
            borderWidth: 2,
          },
          {
            label: "FIE2",
            data: [70, 50, 60, 40, 55],
            backgroundColor: "rgba(156, 136, 255, 0.2)",
            borderColor: "#9C88FF",
            borderWidth: 2,
          },
        ],
      },
      options: {
        responsive: true,
        animation: {
          duration: 1500,
          easing: "easeInOutCubic",
        },
        plugins: {
          tooltip: {
            enabled: true,
            backgroundColor: "rgba(0, 0, 0, 0.8)",
            titleFont: { size: 14 },
            bodyFont: { size: 12 },
          },
          legend: { position: "bottom" },
        },
      },
    });
  }
});

// Fonction pour créer un dégradé
function createGradient(chart, color1, color2) {
  const ctx = chart.getContext("2d");
  const gradient = ctx.createLinearGradient(0, 0, 0, chart.height);
  gradient.addColorStop(0, color1);
  gradient.addColorStop(1, color2);
  return gradient;
}
</script>

<style scoped>
/* 📌 Conteneur principal */
.dashboard-container {
  padding: 20px;
  font-family: 'Inter', sans-serif;
  max-width: 1300px;
  margin: 0 auto;
  background-color: #f8f9fa;
}

/* 📌 Barre de recherche */
.search-container {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
}

.search-input {
  width: 50%;
  padding: 12px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  outline: none;
  transition: all 0.3s ease;
}

.search-input:focus {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transform: scale(1.02);
}

/* 📌 Indicateurs Clés (KPI) */
.kpi-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-gap: 20px;
  margin-bottom: 20px;
}

.kpi-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  padding: 15px;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  border: 1px solid rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  height: 100px; /* Hauteur réduite pour un design plus fin */
}

.kpi-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.kpi-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.kpi-icon {
  font-size: 24px;
  color: #6a3fa0;
}

.kpi-text {
  text-align: left;
}

.kpi-card h3 {
  font-size: 14px;
  color: #6a3fa0;
  margin-bottom: 5px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.kpi-value {
  font-size: 20px;
  font-weight: 700;
  color: #2c3e50;
  margin: 5px 0;
}

.kpi-trend {
  font-size: 12px;
  color: #4caf50;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 5px;
}

.kpi-trend::before {
  content: "↑";
  font-size: 10px;
  color: #4caf50;
}

/* 📌 Grille du Dashboard */
.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-gap: 20px;
  width: 100%;
  height: 100%;
}

/* 📌 Cartes */
.card {
  background: linear-gradient(135deg, #ffffff 0%, #f0f7ff 100%);
  padding: 25px;
  border-radius: 15px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

/* 📌 Cartes des graphiques */
.chart-card h2 {
  font-size: 18px;
  color: #6a3fa0;
  margin-bottom: 15px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 1px;
}

/* 📌 Canvas */
canvas {
  width: 100%;
  height: 25vh; /* Ajustement dynamique de la hauteur */
}

/* 📌 Responsive */
@media (max-width: 900px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }

  .kpi-grid {
    grid-template-columns: 1fr;
  }

  canvas {
    height: 30vh; /* Ajustement pour mobile */
  }
}
</style>