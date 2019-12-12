import { Pipe, PipeTransform } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Pipe({
  name: 'FrequencyFormat',
  pure: false
})
export class FrequencyFormatPipe implements PipeTransform {
  suffixes;

  constructor(private translateService: TranslateService) {
    this.suffixes = [
      'th', // 0
      'st', // 1
      'nd', // 2
      'rd', // 3
      'th', // 4
      'th', // 5
      'th', // 6
      'th', // 7
      'th', // 8
      'th', // 9
      'th', // 10
      'th', // 11
      'th', // 12
      'th', // 13
      'th', // 14
      'th', // 15
      'th', // 16
      'th', // 17
      'th', // 18
      'th', // 19
      'th', // 20
      'st', // 21
      'nd', // 22
      'rd', // 23
      'th', // 24
      'th', // 25
      'th', // 26
      'th', // 27
      'th', // 28
      'th', // 29
      'th', // 30
      'st' // 31
    ];
  }

  transform(frequency: string): string {
    //"^(EvryDay)$|^(EvryWorkgDay)$|^(IntrvlWkDay:0[1-9]:0[1-7])$|^(WkInMnthDay:0[1-5]:0[1-7])$|^(IntrvlMnthDay:(0[1-6]|12|24):(-0[1-5]|0[1-9]|[12][0-9]|3[01]))$|^(QtrDay:(ENGLISH|SCOTTISH|RECEIVED))$"
    if (new RegExp('^EvryDay$').test(frequency)) {
      return this.translateService.instant('FREQUENCY.EvryDay');
    }
    if (new RegExp('^EvryWorkgDay$').test(frequency)) {
      return this.translateService.instant('FREQUENCY.EvryWorkgDay');
    }

    const intrvlWkDayreg = new RegExp('^IntrvlWkDay:(0[1-9]):(0[1-7])$');
    if (intrvlWkDayreg.test(frequency)) {
      const variable = intrvlWkDayreg.exec(frequency);
      const weekNumber = +variable[1];
      const dayNumber = +variable[2];

      let weekNumberFormatted = '';
      if (weekNumber !== 1) {
        weekNumberFormatted = weekNumber + this.suffixes[weekNumber];
      }
      const dayNumberFormatted = dayNumber + this.suffixes[dayNumber];
      return this.translateService.instant('FREQUENCY.IntrvlWkDay', {
        weekNumber: weekNumberFormatted,
        dayNumber: dayNumberFormatted
      });
    }

    if (new RegExp('^WkInMnthDay:(0[1-5]):(0[1-7])$').test(frequency)) {
      const variable = intrvlWkDayreg.exec(frequency);
      const weekNumber = +variable[1];
      const dayNumber = +variable[2];

      const weekNumberFormatted = weekNumber + this.suffixes[weekNumber];
      const dayNumberFormatted = dayNumber + this.suffixes[dayNumber];

      return this.translateService.instant('FREQUENCY.WkInMnthDay', {
        weekNumber: weekNumberFormatted,
        dayNumber: dayNumberFormatted
      });
    }

    const IntrvlMnthDayReg = new RegExp('^IntrvlMnthDay:(0[1-6]|12|24):(-0[1-5]|0[1-9]|[12][0-9]|3[01])$');
    if (IntrvlMnthDayReg.test(frequency)) {
      const variable = IntrvlMnthDayReg.exec(frequency);
      const monthNumber = +variable[1];
      const dayNumber = +variable[2];

      let monthNumberFormatted = '';
      if (monthNumber !== 1) {
        monthNumberFormatted = monthNumber + this.suffixes[monthNumber];
      }
      let dayNumberFormatted = dayNumber + this.suffixes[dayNumber];
      if (dayNumber < 0) {
        if (dayNumber === -1) {
          dayNumberFormatted = '';
        }
        return this.translateService.instant('FREQUENCY.IntrvlMnthDayFromEndMonth', {
          monthNumber: monthNumberFormatted,
          dayNumber: dayNumberFormatted
        });
      } else {
        return this.translateService.instant('FREQUENCY.IntrvlMnthDay', {
          monthNumber: monthNumberFormatted,
          dayNumber: dayNumberFormatted
        });
      }
    }

    const qtrDayReg = new RegExp('^QtrDay:(ENGLISH|SCOTTISH|RECEIVED)$');
    if (qtrDayReg.test(frequency)) {
      const variable = IntrvlMnthDayReg.exec(frequency);
      const quarter = variable[1];
      return this.translateService.instant('FREQUENCY.QtrDay.' + quarter);
    }
    return frequency;
  }
}
