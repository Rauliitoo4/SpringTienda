import { Component, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-searchbar',
  imports: [FormsModule, MatIconModule, CommonModule],
  templateUrl: './searchbar.component.html',
  styleUrl: './searchbar.component.scss'
})
export class SearchbarComponent {
  query = signal('');
  searched = output<string>();

  search() {
    this.searched.emit(this.query());
  }

  onKeyDown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.search();
    }
  }
}