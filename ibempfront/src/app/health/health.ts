import {Component, OnInit} from '@angular/core';
import {HealthService} from '../health';
import {JsonPipe} from '@angular/common';

@Component({
  selector: 'app-health',
  imports: [JsonPipe],
  templateUrl: './health.html',
  styleUrl: './health.css'
})
export class Health implements OnInit{

  health!: string

  constructor(private healthService: HealthService) {
  }

  ngOnInit(): void {
    this.healthService.getHealth().subscribe(v => {
      this.health = v
    })
  }

}
