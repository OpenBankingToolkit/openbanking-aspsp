import { Component, OnInit, Input, ChangeDetectionStrategy } from '@angular/core';

import { permissionMocks } from './permissions';
import { OBAccountPermissions } from 'bank/src/app/types/OBAccountPermissions';

@Component({
  selector: 'app-account-permissions',
  templateUrl: './permissions.component.html',
  styleUrls: ['./permissions.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PermissionsComponent implements OnInit {
  @Input() permissions: OBAccountPermissions[];

  constructor() {}

  ngOnInit() {}

  getPermissionMock(name: string) {
    return permissionMocks[name] || '';
  }
}
