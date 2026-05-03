import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-catalog',
  imports: [RouterLink, MatButtonModule, MatIconModule, MatSelectModule, MatFormFieldModule, CommonModule, FormsModule],
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.scss'
})
export class CatalogComponent {
  selectedFilter = signal('todo');
  selectedSort = 'relevantes';

  setFilter(filter: string) {
    this.selectedFilter.set(filter);
  }
}
