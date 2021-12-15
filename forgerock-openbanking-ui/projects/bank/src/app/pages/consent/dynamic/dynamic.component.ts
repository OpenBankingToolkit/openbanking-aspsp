import {
  ChangeDetectionStrategy,
  Component,
  ComponentFactoryResolver,
  ComponentRef,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  ViewChild,
  ViewContainerRef,
  ViewEncapsulation
} from '@angular/core';
import {throwError} from 'rxjs';
import debug from 'debug';

import {ApiResponses} from 'bank/src/app/types/api';
import {SinglePaymentComponent} from '../single-payment/single-payment.component';
import {AccountComponent} from '../account/account.component';
import {IntentType} from 'bank/src/app/types/IntentType';
import {DomesticPaymentComponent} from 'bank/src/app/pages/consent/domestic-payment/domestic-payment.component';
import {DomesticSchedulePaymentComponent} from 'bank/src/app/pages/consent/domestic-schedule-payment/domestic-schedule-payment.component';
import {DomesticStandingOrderComponent} from 'bank/src/app/pages/consent/domestic-standing-order/domestic-standing-order.component';
import {InternationalPaymentComponent} from 'bank/src/app/pages/consent/international-payment/international-payment.component';
import {InternationalSchedulePaymentComponent} from 'bank/src/app/pages/consent/international-schedule-payment/international-schedule-payment.component';
import {InternationalStandingOrderComponent} from 'bank/src/app/pages/consent/international-standing-order/international-standing-order.component';
import {FundsConfirmationComponent} from 'bank/src/app/pages/consent/funds-confirmation/funds-confirmation.component';
import {FilePaymentComponent} from 'bank/src/app/pages/consent/file-payment/file-payment.component';
import {CancelComponent} from "bank/src/app/pages/consent/components/cancel/cancel.component";
import {RejectComponent} from "bank/src/app/pages/consent/components/reject/reject.component";
import {VrpPaymentComponent} from "bank/src/app/pages/consent/vrp-payment/vrp-payment.component";

const log = debug('consent:DynamicComponent');

@Component({
  selector: 'app-consent-dynamic',
  templateUrl: './dynamic.component.html',
  styleUrls: ['./dynamic.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class DynamicComponent implements OnInit, OnChanges {
  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
  }

  @Input() response: ApiResponses.ConsentDetailsResponse;
  @Input() loading: boolean;
  @Output() formSubmit: EventEmitter<any> = new EventEmitter<any>();
  @ViewChild('dynamicTarget', {read: ViewContainerRef, static: true})
  dynamicTarget: ViewContainerRef;
  componentRef: ComponentRef<any>;

  ngOnInit() {
  }

  ngOnChanges(changes: any) {
    console.log("Dynamic component")
    if (this.response.userActions?.rejectedByUser) {
      this.createComponent(CancelComponent);
    } else if (this.response.userActions?.rejectedByUser) {
      this.createComponent(RejectComponent);
    }
    if (changes.loading && !changes.loading.firstChange) {
      this.componentRef.instance.loading = changes.loading.currentValue;
    }
    if (!changes.response || !changes.response.currentValue) {
      return;
    }
    this.create(changes.response.currentValue);
  }

  create(response) {
    console.log(`create consent view: ${response.intentType}`)
    log(`create consent view: ${response.intentType}`);

    let componentInstance;
    switch (response.intentType) {
      case IntentType.PAYMENT_SINGLE_REQUEST:
        componentInstance = SinglePaymentComponent;
        break;
      case IntentType.ACCOUNT_ACCESS_CONSENT:
        componentInstance = AccountComponent;
        break;
      case IntentType.PAYMENT_DOMESTIC_CONSENT:
        componentInstance = DomesticPaymentComponent;
        break;
      case IntentType.PAYMENT_DOMESTIC_SCHEDULED_CONSENT:
        componentInstance = DomesticSchedulePaymentComponent;
        break;
      case IntentType.PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT:
        componentInstance = DomesticStandingOrderComponent;
        break;
      case IntentType.PAYMENT_INTERNATIONAL_CONSENT:
        componentInstance = InternationalPaymentComponent;
        break;
      case IntentType.PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT:
        componentInstance = InternationalSchedulePaymentComponent;
        break;
      case IntentType.PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT:
        componentInstance = InternationalStandingOrderComponent;
        break;
      case IntentType.PAYMENT_FILE_CONSENT:
        componentInstance = FilePaymentComponent;
        break;
      case IntentType.FUNDS_CONFIRMATION_CONSENT:
        componentInstance = FundsConfirmationComponent;
        break;
      case IntentType.DOMESTIC_VRP_PAYMENT_CONSENT:
        componentInstance = VrpPaymentComponent;
        break;
      default:
        log(`"${response.requestType}" consent type is not implemented yet`);
        throwError(`"${response.requestType}" consent type is not implemented yet`);
        // this.cdr.detectChanges();
        return;
    }

    this.createComponent(componentInstance);
  }

  createComponent(componentInstance: any) {
    // Select, clear and inject the dynamic component with props data
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(componentInstance);
    this.dynamicTarget.clear();
    this.componentRef = this.dynamicTarget.createComponent(componentFactory);
    this.componentRef.instance.response = this.response;
    this.componentRef.instance.loading = this.loading;
    this.componentRef.instance.formSubmit = this.formSubmit;
  }
}
