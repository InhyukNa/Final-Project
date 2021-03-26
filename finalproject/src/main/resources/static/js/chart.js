$(document).ready(function() {
   /**index meat Male/feMale Bar Chart Start */
   var maleproname1 = [];
   var malesalestock1 = [];
   var femaleproname1 = [];
   var femalesalestock1 = [];
   var maleproname2 = [];
   var malesalestock2 = [];
   var femaleproname2 = [];
   var femalesalestock2 = [];
   var maleproname3 = [];
   var malesalestock3 = [];
   var femaleproname3 = [];
   var femalesalestock3 = [];
   var clscode1 = $('#indexChartbtn1').val();
   var clscode2 = $('#indexChartbtn2').val();
   var clscode3 = $('#indexChartbtn3').val();
   var backgroundColor = 
      ['rgba(255, 99, 132, 0.2)',
      'rgba(54, 162, 235, 0.2)',
      'rgba(120, 52, 50, 0.2)',
      'rgba(99, 84, 20, 0.2)',
      'rgba(200, 120, 230, 0.2)',
      'rgba(170, 20, 180, 0.2)',
      'rgba(221, 2, 92, 0.2)',
      'rgba(142, 37, 168, 0.2)',
      'rgba(196, 37, 140, 0.2)',
      'rgba(112, 77, 250, 0.2)',
      'rgba(255, 55, 70, 0.2)'];
   var borderColor = 
      ['rgba(255, 99, 132, 1)',
      'rgba(54, 162, 235, 1)',
      'rgba(120, 52, 50, 1)',
      'rgba(99, 84, 20, 1)',
      'rgba(200, 120, 230, 1)',
      'rgba(170, 20, 180, 1)',
      'rgba(221, 2, 92, 1)',
      'rgba(142, 37, 168, 1)',
      'rgba(196, 37, 140, 1)',
      'rgba(112, 77, 250, 1)',
      'rgba(255, 55, 70, 1)']
   $.ajax({
      type: 'POST',
      datatype: 'json',
      data: { clscode: clscode1 },
      url: 'maleBestSaleSelect',
      success: function(data) {
         for (var i = 0; i < data.length; i++) {
            maleproname1.push(data[i].salename);
            malesalestock1.push(data[i].salestock);
         }

      },
      error: function(xhr, status, error) {
         alert('ajax error : ' + xhr.status + error);
      }
   });

   $.ajax({
      type: 'POST',
      datatype: 'json',
      data: { clscode: clscode1 },
      url: 'femaleBestSaleSelect',
      success: function(data) {
         for (var i = 0; i < data.length; i++) {
            femaleproname1.push(data[i].salename);
            femalesalestock1.push(data[i].salestock);
         }

      },
      error: function(xhr, status, error) {
         alert('ajax error : ' + xhr.status + error);
      }
   });

   $("#indexChartbtn1").on('click', function() {
      $('#indexMaleChart0').css("display", 'none');
      $('#indexMaleChart1').css("display", 'block');
      $('#indexMaleChart2').css("display", 'none');
      $('#indexMaleChart3').css("display", 'none');

      $('#indexfeMaleChart0').css("display", 'none');
      $('#indexfeMaleChart1').css("display", 'block');
      $('#indexfeMaleChart2').css("display", 'none');
      $('#indexfeMaleChart3').css("display", 'none');
      var ctx = document.getElementById("indexMaleChart1").getContext('2d');
      var indexMaleChart1 = new Chart(ctx, {
         type: 'bar',
         data: {
            labels: maleproname1,
            datasets: [{
               data: malesalestock1, //컨트롤러에서 모델로 받아온다.
               backgroundColor: backgroundColor,
               borderColor: borderColor,
               borderWidth: 1
            }
            ]
         },
         options: {
            scales: {
               yAxes: [{
                  ticks: { beginAtZero: true }
               }]
            },
            legend: { display: false},
            title: {
               display:true,
               text: '남성 단백질류 판매량 TOP3'
            }
         }
      });

      var ctx = document.getElementById("indexfeMaleChart1").getContext('2d');
      var indexfeMaleChart1 = new Chart(ctx, {
         type: 'bar',
         data: {
            labels: femaleproname1,
            datasets: [{
               data: femalesalestock1, //컨트롤러에서 모델로 받아온다.
               backgroundColor: backgroundColor,
               borderColor: borderColor,
               borderWidth: 1
            }
            ]
         },
         options: {
            scales: {
               yAxes: [{
                  ticks: { beginAtZero: true }
               }]
            },
            legend: { display: false},
            title: {
               display:true,
               text: '여성 단백질류 판매량 TOP3'
            }
         }
      });
   });
   /*index meat male/female Bar Chart END*/

   /*index vegetable male/female Bar Chart END*/
   $.ajax({
      type: 'POST',
      datatype: 'json',
      data: { clscode: clscode2 },
      url: 'maleBestSaleSelect',
      success: function(data) {
         for (var i = 0; i < data.length; i++) {
            maleproname2.push(data[i].salename);
            malesalestock2.push(data[i].salestock);
         }

      },
      error: function(xhr, status, error) {
         alert('ajax error : ' + xhr.status + error);
      }
   });

   $.ajax({
      type: 'POST',
      datatype: 'json',
      data: { clscode: clscode2 },
      url: 'femaleBestSaleSelect',
      success: function(data) {
         for (var i = 0; i < data.length; i++) {
            femaleproname2.push(data[i].salename);
            femalesalestock2.push(data[i].salestock);
         }

      },
      error: function(xhr, status, error) {
         alert('ajax error : ' + xhr.status + error);
      }
   });

   $("#indexChartbtn2").on('click', function() {
      $('#indexMaleChart0').css("display", 'none');
      $('#indexMaleChart1').css("display", 'none');
      $('#indexMaleChart2').css("display", 'block');
      $('#indexMaleChart3').css("display", 'none');

      $('#indexfeMaleChart0').css("display", 'none');
      $('#indexfeMaleChart1').css("display", 'none');
      $('#indexfeMaleChart2').css("display", 'block');
      $('#indexfeMaleChart3').css("display", 'none');

      var ctx = document.getElementById("indexMaleChart2").getContext('2d');
      var indexMaleChart2 = new Chart(ctx, {
         type: 'bar',
         data: {
            labels: maleproname2,
            datasets: [{
               data: malesalestock2, //컨트롤러에서 모델로 받아온다.
               backgroundColor: backgroundColor,
               borderColor: borderColor,
               borderWidth: 1
            }
            ]
         },
         options: {
            scales: {
               yAxes: [{
                  ticks: { beginAtZero: true }
               }]
            },
            legend: { display: false},
            title: {
               display:true,
               text: '남성 채소류 판매량 TOP3'
            }
         }
      });

      var ctx = document.getElementById("indexfeMaleChart2").getContext('2d');
      var indexfeMaleChart2 = new Chart(ctx, {
         type: 'bar',
         data: {
            labels: femaleproname2,
            datasets: [{
               data: femalesalestock2, //컨트롤러에서 모델로 받아온다.
               backgroundColor: backgroundColor,
               borderColor: borderColor,
               borderWidth: 1
            }
            ]
         },
         options: {
            scales: {
               yAxes: [{
                  ticks: { beginAtZero: true }
               }]
            },
            legend: { display: false},
            title: {
               display:true,
               text: '여성 채소류 판매량 TOP3'
            }
         }
      });
   });
   /*index vegetable male/female Bar Chart END*/

   /*index sauce male/female Bar Chart START*/
   $.ajax({
      type: 'POST',
      datatype: 'json',
      data: { clscode: clscode3 },
      url: 'maleBestSaleSelect',
      success: function(data) {
         for (var i = 0; i < data.length; i++) {
            maleproname3.push(data[i].salename);
            malesalestock3.push(data[i].salestock);
         }

      },
      error: function(xhr, status, error) {
         alert('ajax error : ' + xhr.status + error);
      }
   });

   $.ajax({
      type: 'POST',
      datatype: 'json',
      data: { clscode: clscode3 },
      url: 'femaleBestSaleSelect',
      success: function(data) {
         for (var i = 0; i < data.length; i++) {
            femaleproname3.push(data[i].salename);
            femalesalestock3.push(data[i].salestock);
         }

      },
      error: function(xhr, status, error) {
         alert('ajax error : ' + xhr.status + error);
      }
   });

   $("#indexChartbtn3").on('click', function() {
      $('#indexMaleChart0').css("display", 'none');
      $('#indexMaleChart1').css("display", 'none');
      $('#indexMaleChart2').css("display", 'none');
      $('#indexMaleChart3').css("display", 'block');
      var ctx = document.getElementById("indexMaleChart3").getContext('2d');
      var indexMaleChart3 = new Chart(ctx, {
         type: 'bar',
         data: {
            labels: maleproname3,
            datasets: [{
               data: malesalestock3, //컨트롤러에서 모델로 받아온다.
               backgroundColor: backgroundColor,
               borderColor: borderColor,
               borderWidth: 1
            }
            ]
         },
         options: {
            scales: {
               yAxes: [{
                  ticks: { beginAtZero: true }
               }]
            },
            legend: { display: false},
            title: {
               display:true,
               text: '남성 소스류 판매량 TOP3'
            }
         }
      });
   });

   $("#indexChartbtn3").on('click', function() {
      $('#indexfeMaleChart0').css("display", 'none');
      $('#indexfeMaleChart1').css("display", 'none');
      $('#indexfeMaleChart2').css("display", 'none');
      $('#indexfeMaleChart3').css("display", 'block');
      var ctx = document.getElementById("indexfeMaleChart3").getContext('2d');
      var indexfeMaleChart3 = new Chart(ctx, {
         type: 'bar',
         data: {
            labels: femaleproname3,
            datasets: [{
               data: femalesalestock3, //컨트롤러에서 모델로 받아온다.
               backgroundColor: backgroundColor,
               borderColor: borderColor,
               borderWidth: 1
            }
            ]
         },
         options: {
            scales: {
               yAxes: [{
                  ticks: { beginAtZero: true }
               }]
            },
            legend: { display: false},
            title: {
               display:true,
               text: '남성 소스류 판매량 TOP3'
            }
         }
      });
   });
   /*index meat male/female Bar Chart END*/
});