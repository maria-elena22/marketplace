import {Component, Inject} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

export interface DialogData {
  animal: string;
  name: string;
}

@Component({
  selector: 'app-terminar-sessao',
  templateUrl: './terminar-sessao.component.html',
  styleUrls: ['./terminar-sessao.component.css']
})
export class TerminarSessaoComponent {
  animal: string;
  name: string;
  
  constructor(public dialog: MatDialog) {}

  openDialog(): void {
    const dialogRef = this.dialog.open(TerminarSessaoComponent, {
      width: '250px',
      data: {name: this.name, animal: this.animal},
      backdropClass: 'backdropBackground'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.animal = result;
    });
  }
}
