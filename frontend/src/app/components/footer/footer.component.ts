import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { siGithub, siInstagram, siTwitch } from 'simple-icons';

@Component({
  selector: 'app-footer',
  imports: [RouterLink],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
export class FooterComponent {
  currentYear = new Date().getFullYear();
  siGithub = siGithub;
  siInstagram = siInstagram;
  siTwitch = siTwitch;
}
