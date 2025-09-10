import { Routes } from '@angular/router';
import {Health} from './health/health';
import {CsvUpload} from './csv-upload/csv-upload';

export const routes: Routes = [
  { path: 'health', component: Health },
  { path: 'csv-upload', component: CsvUpload }
];
