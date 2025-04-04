-- Table referent
INSERT INTO referent (nom, prenom, email)
VALUES ('Fleuranceau', 'Manon', 'manon.fleuranceau@univ-jfc.fr');

INSERT INTO referent (nom, prenom, email)
VALUES ('Libouroux', 'Sandrine', 'sandrine.libouroux@univ-jfc.fr');

INSERT INTO referent (nom, prenom, email)
VALUES ('Barreau', 'Sylvain', 'sylvain.barreau@univ-jfc.fr');

-- Table referentiel
INSERT INTO referentiel (nom, description)
VALUES
    ('Engagement associatif', 'Actions en lien avec la vie associative et les responsabilités étudiantes'),
    ('Engagement éducatif', 'Actions liées au mentorat, tutorat et accompagnement scolaire'),
    ('Engagement écologique', 'Actions liées à la transition écologique et à la sensibilisation'),
    ('Engagement promotionnel', 'Actions de promotion de l’école et participation aux événements');

INSERT INTO referentiel (id_referentiel, nom, description)
VALUES (-1, 'Autre', 'Autre');

-- Table action
-- Suppression de la colonne id_referent
INSERT INTO action (nom, description_Action, id_referentiel, date_Debut_Action, date_Fin_Action)
VALUES
-- Engagement Associatif
('Membre du bureau d’une association', 'Participation active en tant que membre du bureau d’une association étudiante.', 1, '2025-01-10', '2025-12-31'),
('Contribution à la vie associative', 'Participation à des projets associatifs sur le campus.', 1, '2025-02-15', '2025-12-31'),
('Engagement hors campus', 'Bénévolat pour des associations externes (ex: Croix Rouge, pompiers volontaires).', 1, '2025-03-01', '2025-12-31'),
-- Engagement Éducatif
('La Main à la Pâte', 'Tutorat et soutien scolaire pour les élèves.', 2, '2025-02-02', '2025-12-31'),
('Mentorat avec AFEV', 'Accompagnement d’élèves en difficulté via le programme AFEV.', 2, '2025-03-10', '2025-12-31'),
('Aide aux devoirs', 'Soutien scolaire pour élèves du primaire et du secondaire.', 2, '2025-04-01', '2025-12-31'),
-- Engagement Écologique
('Fresque du Climat', 'Animation d’ateliers sur le climat et l’écologie.', 3, '2025-03-01', '2025-12-31'),
('Fresque du Numérique', 'Sensibilisation aux enjeux environnementaux du numérique.', 3, '2025-03-20', '2025-12-31'),
('Campus en Transition', 'Organisation d’événements pour une transition écologique sur le campus.', 3, '2025-04-15', '2025-12-31'),
-- Engagement Promotionnel
('Ambassadeur ISIS', 'Représentation de l’école lors d’événements.', 4, '2025-02-01', '2025-12-31'),
('Animation JPO', 'Participation aux Journées Portes Ouvertes pour présenter les formations.', 4, '2025-02-05', '2025-02-06'),
('Encadrement de lycéens', 'Accompagnement de lycéens dans la découverte des études supérieures.', 4, '2025-03-12', '2025-12-31');

INSERT INTO action (id_Action, nom, description_Action, date_Debut_Action, date_Fin_Action)
VALUES (-1, 'Autre', 'Autre', NULL, NULL);

-- Table etudiant
INSERT INTO etudiant (nom, prenom, email, promotion)
VALUES ('Ines', 'Gribaa', 'Ines.Griba@etud.univ-jfc.fr', 'FIE 3');

INSERT INTO etudiant (nom, prenom, email, promotion)
VALUES ('Bernard', 'Luc', 'luc.bernard@etud.univ-jfc.fr', 'FIE 3');

INSERT INTO etudiant (nom, prenom, email, promotion)
VALUES ('Moreau', 'Emma', 'emma.moreau@etud.univ-jfc.fr', 'FIE 4');

-- Table semestre
INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre)
VALUES ('2025-06-02','2025-12-02', 1);

INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre)
VALUES ('2026-02-03','2025-02-03', 2);

-- Table participe
-- Ajout de la colonne id_referent pour associer éventuellement un référent validant la participation
INSERT INTO participe (id_etudiant, id_action, id_semestre, id_referent, total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation, description_Participation, statut)
VALUES
-- 🌍 Engagement Associatif : participation validée par le référent avec id 1 (statut TRUE)
(1, 2, 1, 1, 0.25, 1, '2025-04-01', '2025-04-02', 'Participation occasionnelle à un projet associatif', TRUE),

-- 🎓 Engagement Éducatif : participation non validée (id_referent à NULL)
(2, 5, 2, NULL, 0, 1, '2025-03-10', '2025-03-12', 'Participation partielle au tutorat AFEV', NULL),

-- 🌱 Engagement Écologique : participation non validée (id_referent à NULL)
(3, 8, 1, NULL, 0.40, 1, '2025-05-20', '2025-05-21', 'Présence ponctuelle à un atelier Fresque du Climat', FALSE);

-- Table role
INSERT INTO role (name) VALUES ('ROLE_ETUDIANT');
INSERT INTO role (name) VALUES ('ROLE_DIRECTEUR');
INSERT INTO role (name) VALUES ('ROLE_REFERENT');
INSERT INTO role (name) VALUES ('ROLE_SERVICE_SCOLARITE');