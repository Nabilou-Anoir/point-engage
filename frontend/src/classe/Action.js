export class Action {
  constructor(idAction, nom, descriptionAction, dateDebutAction, dateFinAction) {
    this._idAction = idAction;
    this._nom = nom;
    this._descriptionAction = descriptionAction;
    this._dateDebutAction = dateDebutAction;
    this._dateFinAction = dateFinAction;
  }

  get idAction() { return this._idAction; }
  set idAction(value) { this._idAction = value; }

  get nom() { return this._nom; }
  set nom(value) { this._nom = value; }

  get descriptionAction() { return this._descriptionAction; }
  set descriptionAction(value) { this._descriptionAction = value; }

  get dateDebutAction() { return this._dateDebutAction; }
  set dateDebutAction(value) { this._dateDebutAction = value; }

  get dateFinAction() { return this._dateFinAction; }
  set dateFinAction(value) { this._dateFinAction = value; }
}
