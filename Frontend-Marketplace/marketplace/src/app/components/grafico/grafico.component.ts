import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-grafico',
  templateUrl: './grafico.component.html',
  styleUrls: ['./grafico.component.css']
})
export class GraficoComponent implements OnInit {
  @ViewChild('myChart') myChartElementRef: ElementRef;
  chart: Chart;

  ngOnInit(){}

  ngAfterViewInit() {
    this.createChart();
  }

  createChart() {
  }
}