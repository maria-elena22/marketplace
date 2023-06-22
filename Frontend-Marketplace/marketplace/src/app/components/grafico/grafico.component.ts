import { Component, AfterViewInit, ViewChild, ElementRef, Input } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-grafico',
  templateUrl: './grafico.component.html',
  styleUrls: ['./grafico.component.css']
})
export class GraficoComponent implements AfterViewInit {
  
  @ViewChild('histogramaCanvas', { static: false }) histogramaCanvas: ElementRef;

  @Input() dadosHist:Array<number> ; //= [10, 20, 30, 50, 60, 40]; 
  @Input() labelsHist:Array<string> ; // ['America', 'Europa', 'Oceania', 'Asia']
  chartInstance: Chart

  ngAfterViewInit() {
    
    this.criarHistograma();


  }


  criarHistograma() {
    const canvas = this.histogramaCanvas.nativeElement;
    if (this.chartInstance) {
      this.chartInstance.destroy();
    }    
    const ctx = canvas.getContext('2d');
    
    this.chartInstance = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: this.labelsHist,
        datasets: [{
          label: "Número de Encomendas",
          data: this.dadosHist,
          // fill:true,
          backgroundColor: 'rgba(78, 115, 223, 0.2)',
          borderColor: 'rgba(78, 115, 223, 1)',
          borderWidth: 1
        }]
      },

    options: {
      // plugins: {
      //   title: {
      //     display: true,
      //     text: this.titulo, // Adicione o título desejado aqui
      //     font: {
      //       size: 16 // Defina o tamanho da fonte do título
      //     }
      //   }
      // },
      maintainAspectRatio: false, 
      scales: {
        x: {
          position: 'bottom',
          ticks: {
            font: {
              size: 16 
            }
          }
        },
        y: {
          beginAtZero: true,
          ticks: {
            font: {
              size: 16 
            }
          }
        }
      }
    }
  });
}








  
}