import { Component, input, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Size } from '../../models/product/size.model';

@Component({
  selector: 'app-size-selector',
  imports: [CommonModule],
  templateUrl: './size-selector.component.html',
  styleUrl: './size-selector.component.scss'
})
export class SizeSelectorComponent {
  sizes = input<Size[]>([]);
  productName = input<string>('');
  closed = output<void>();
  sizeSelected = output<{ size: Size, quantity: number }>();

  selectedSize = signal<Size | null>(null);
  quantity = signal(1);

  select(size: Size) {
    this.selectedSize.set(size);
  }

  increase() {
    this.quantity.update(v => v + 1);
  }

  decrease() {
    if (this.quantity() > 1) this.quantity.update(v => v - 1);
  }

  confirm() {
    const size = this.selectedSize();
    if (!size) return;
    this.sizeSelected.emit({ size, quantity: this.quantity() });
  }

  close() {
    this.closed.emit();
  }
}