import { Component } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import {JsonPipe, NgForOf, NgIf} from '@angular/common';
import {CalcData} from './CalcData';

@Component({
  selector: 'app-csv-upload',
  standalone: true,
  imports: [NgIf, NgForOf, JsonPipe],
  templateUrl: './csv-upload.html'
})
export class CsvUpload {
  selectedFile: File | null = null
  csvData: string[][] = []

  calcData: CalcData[] = []

  constructor(private http: HttpClient) {}

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement
    this.selectedFile = input.files?.[0] || null
  }

  upload() {
    if (!this.selectedFile) return

    const formData = new FormData()
    formData.append('file', this.selectedFile)

    this.http.post<string[][]>('http://localhost:8080/upload', formData)
      .subscribe(data => this.csvData = data)
  }

  calculate() {
    if (!this.selectedFile) return

    const formData = new FormData()
    formData.append('file', this.selectedFile)

    this.http.get<CalcData[]>('http://localhost:8080/calculate')
      .subscribe(data => { this.calcData = data
      })
  }
}
