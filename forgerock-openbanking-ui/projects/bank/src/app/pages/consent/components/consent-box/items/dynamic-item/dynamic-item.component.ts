import {
  Component,
  ComponentFactoryResolver,
  Input,
  OnChanges,
  OnInit,
  ViewChild,
  ViewContainerRef
} from '@angular/core';
import { throwError } from 'rxjs';
import { Item, ItemType } from 'bank/src/app/types/consentItem';
import { StringItemComponent } from 'bank/src/app/pages/consent/components/consent-box/items/string-item/string-item.component';
import { RateAmountItemComponent } from 'bank/src/app/pages/consent/components/consent-box/items/rate-amount-item/rate-amount-item.component';
import { NextPaymentItemComponent } from 'bank/src/app/pages/consent/components/consent-box/items/next-payment-item/next-payment-item.component';
import { InstructedAmountItemComponent } from 'bank/src/app/pages/consent/components/consent-box/items/instructed-amount-item/instructed-amount-item.component';
import { FirstPaymentItemComponent } from 'bank/src/app/pages/consent/components/consent-box/items/first-payment-item/first-payment-item.component';
import { FinalPaymentItemComponent } from 'bank/src/app/pages/consent/components/consent-box/items/final-payment-item/final-payment-item.component';
import { ExchangeRateItemComponent } from 'bank/src/app/pages/consent/components/consent-box/items/exchange-rate-item/exchange-rate-item.component';
import { DateItemComponent } from 'bank/src/app/pages/consent/components/consent-box/items/date-item/date-item.component';
import { AccountNumberItemComponent } from 'bank/src/app/pages/consent/components/consent-box/items/account-number-item/account-number-item.component';
import { TransactionsPeriodItemComponent } from 'bank/src/app/pages/consent/components/consent-box/items/transactions-period-item/transactions-period-item.component';
import {VrpAccountItemComponent} from "bank/src/app/pages/consent/components/consent-box/items/vrp-account-item/vrp-account-item.component";

@Component({
  selector: 'app-dynamic-item',
  templateUrl: './dynamic-item.component.html',
  styleUrls: ['./dynamic-item.component.scss']
})
export class DynamicItemComponent implements OnInit, OnChanges {
  @Input() item: Item;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {}

  @ViewChild('dynamicTarget', { read: ViewContainerRef, static: true })
  dynamicTarget: ViewContainerRef;

  ngOnInit(): void {}

  ngOnChanges(changes: any) {
    if (!changes.item || !changes.item.currentValue) return;

    this.create(changes.item.currentValue);
  }

  create(item) {
    // console.trace(`create consent view: ${response.intentType}`);
    let componentInstance;
    switch (item.type) {
      case ItemType.STRING:
        componentInstance = StringItemComponent;
        break;
      case ItemType.RATE_AMOUNT:
        componentInstance = RateAmountItemComponent;
        break;
      case ItemType.NEXT_PAYMENT:
        componentInstance = NextPaymentItemComponent;
        break;
      case ItemType.INSTRUCTED_AMOUNT:
        componentInstance = InstructedAmountItemComponent;
        break;
      case ItemType.FIRST_PAYMENT:
        componentInstance = FirstPaymentItemComponent;
        break;
      case ItemType.FINAL_PAYMENT:
        componentInstance = FinalPaymentItemComponent;
        break;
      case ItemType.EXCHANGE_RATE:
        componentInstance = ExchangeRateItemComponent;
        break;
      case ItemType.DATE:
        componentInstance = DateItemComponent;
        break;
      case ItemType.ACCOUNT_NUMBER:
        componentInstance = AccountNumberItemComponent;
        break;
      case ItemType.TRANSACTION_PERIOD:
        componentInstance = TransactionsPeriodItemComponent;
        break;
      case ItemType.VRP_ACCOUNT_NUMBER:
        componentInstance = VrpAccountItemComponent;
        break;
      default:
        console.error(`"${item.type}" consent type is not implemented yet`);
        throwError(`"${item.type}" consent type is not implemented yet`);
        // this.cdr.detectChanges();
        return;
    }
    const component = this.createComponent(componentInstance);
    component.payload = item.payload;
  }

  createComponent(componentInstance) {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(componentInstance);
    this.dynamicTarget.clear();
    const componentRef = this.dynamicTarget.createComponent(componentFactory);
    return <any>componentRef.instance;
  }
}
