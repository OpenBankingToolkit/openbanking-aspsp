import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'register-toolbar-menu',
  templateUrl: './register-toolbar-menu.component.html'
})
export class RegisterToolbarMenuComponent implements OnInit {
  @Input() connected: boolean;
  @Output() logout = new EventEmitter<Event>();

  constructor() {}

  ngOnInit(): void {}

  onLogout(e: Event) {
    this.logout.emit(e);
  }
}
