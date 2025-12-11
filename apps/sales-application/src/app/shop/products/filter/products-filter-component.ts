import { Component, effect, inject, input, output } from '@angular/core';
import { FilterProductsFormContent, ProductFilter, ProductFilterForm, sizes } from '../../../admin/model/product.model';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl,FormRecord,ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'ecom-products-filter-component',
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './products-filter-component.html',
  styleUrl: './products-filter-component.scss',
})
export class ProductsFilterComponent {

  //sort by createdDate with the newest at the top
  sort = input<string>("createdDate,asc");
  // to be given by the product component later
  size = input<string>();

  // products-filter-component will emit ProductFilter object as below
  productFilter= output<ProductFilter>();


  formBuilder = inject(FormBuilder);

  constructor(){
    //listening to the event
    effect(()=> this.updateSizeFormValue());
    effect(()=> this.updateSortFormValue());
    //subscription taking place below
    this.formFilterProducts.valueChanges.subscribe(()=> this.onFilterChange(this.formFilterProducts.getRawValue()));
  }


  // filtering will be done by two criteria; sort & size
  formFilterProducts = this.formBuilder.nonNullable.group<FilterProductsFormContent>({
    sort: new FormControl<string>(this.sort().split(',')[1], {nonNullable:true,validators: [Validators.required]}),
    size: this.buildSizeFormController()
  });


  private buildSizeFormController(): FormRecord<FormControl<boolean>>{
  const sizeFromControl = this.formBuilder.nonNullable.record<FormControl<boolean>>({});
  for( const size of sizes ){
    sizeFromControl.addControl(size,new FormControl<boolean>(false,{nonNullable:true}));
    }
    return sizeFromControl;
  }


    private onFilterChange(filter: Partial<ProductFilterForm>) {
    const filterProduct: ProductFilter = {
      size: '',
      sort: [`createdDate,${filter.sort}`],
    };

    let sizes: [string, boolean][] = [];
    if (filter.size !== undefined) {
      sizes = Object.entries(filter.size);
    }

    for (const [sizeKey, sizeValue] of sizes) {
      if (sizeValue) {
        if (filterProduct.size?.length === 0) {
          filterProduct.size = sizeKey;
        } else {
          filterProduct.size += `,${sizeKey}`;
        }
      }
    }
    this.productFilter.emit(filterProduct);
  }

  public getSizeFormGroup(): FormRecord<FormControl<boolean>> {
    return  this.formFilterProducts.get('size') as FormRecord<FormControl<boolean>>;
  }

  private updateSizeFormValue():void{
    if(this.size()){
      //if the signal is changing here, then we split all the values separated by the delimiter
      const sizes = this.size()!.split(',');
      for( const size of sizes ){
        //for each value found in the FormRecord control we set the value to true and we do not emit any event,
        // because we do not want to trigger the new onFilter change as above, because we done subscribed to any valueChanges
        // in the constructor
        this.getSizeFormGroup().get(size)!.setValue(true,{emitEvent: false});
      }
    }
  }

  private updateSortFormValue(): void {
    if (this.sort()) {
      this.formFilterProducts.controls.sort.setValue(this.sort().split(',')[1], {emitEvent: false});
    }
  }
  protected readonly sizes = sizes;
}
