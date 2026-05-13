/**
 * Address Selector Component
 * Implements province-city-district linkage selection and detailed address positioning based on Amap API
 */

class AddressSelector {
    constructor(options) {
        this.containerId = options.containerId; // Container ID
        this.onAddressSelected = options.onAddressSelected; // Address selection callback
        this.map = null;
        this.marker = null;
        this.geocoder = null;
        this.districtSearch = null;
        
        this.selectedProvince = '';
        this.selectedCity = '';
        this.selectedDistrict = '';
        this.detailedAddress = '';
        this.longitude = null;
        this.latitude = null;
        
        this.init();
    }
    
    init() {
        this.createUI();
        this.initMap();
        this.loadProvinces();
    }
    
    createUI() {
        const container = document.getElementById(this.containerId);
        container.innerHTML = `
            <div class="address-selector-container">
                <div class="row mb-3">
                    <div class="col-md-4">
                        <label>Province</label>
                        <select id="province-select" class="form-control">
                            <option value="">Please select province</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label>City</label>
                        <select id="city-select" class="form-control" disabled>
                            <option value="">Please select province first</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label>District/County</label>
                        <select id="district-select" class="form-control" disabled>
                            <option value="">Please select city first</option>
                        </select>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-12">
                        <label>Detailed Address</label>
                        <div class="input-group">
                            <input type="text" id="detailed-address" class="form-control" placeholder="Please enter detailed address (street, house number, etc.)">
                            <button class="btn btn-primary" id="locate-btn" type="button">
                                <i class="fa fa-map-marker"></i> Locate
                            </button>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-12">
                        <div id="address-map" style="width: 100%; height: 400px;"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="alert alert-info" id="address-info">
                            <strong>Current Location:</strong><span id="current-address">Not selected</span>
                        </div>
                    </div>
                </div>
            </div>
        `;
        
        this.bindEvents();
    }
    
    bindEvents() {
        const self = this;
        
        // Province selection
        document.getElementById('province-select').addEventListener('change', function() {
            self.selectedProvince = this.value;
            self.selectedCity = '';
            self.selectedDistrict = '';
            self.loadCities(this.value);
            document.getElementById('city-select').disabled = !this.value;
            document.getElementById('district-select').disabled = true;
        });
        
        // City selection
        document.getElementById('city-select').addEventListener('change', function() {
            self.selectedCity = this.value;
            self.selectedDistrict = '';
            self.loadDistricts(this.value);
            document.getElementById('district-select').disabled = !this.value;
        });
        
        // 区县选择
        document.getElementById('district-select').addEventListener('change', function() {
            self.selectedDistrict = this.value;
        });
        
        // 定位按钮
        document.getElementById('locate-btn').addEventListener('click', function() {
            self.locateAddress();
        });
        
        // 详细地址输入
        document.getElementById('detailed-address').addEventListener('input', function() {
            self.detailedAddress = this.value;
        });
    }
    
    initMap() {
        const self = this;
        
        // 初始化地图
        this.map = new AMap.Map('address-map', {
            zoom: 13,
            center: [116.397428, 39.90923] // 默认北京
        });
        
        // 初始化地理编码
        AMap.plugin('AMap.Geocoder', function() {
            self.geocoder = new AMap.Geocoder();
        });
        
        // 初始化行政区划查询
        AMap.plugin('AMap.DistrictSearch', function() {
            self.districtSearch = new AMap.DistrictSearch({
                level: 'province',
                subdistrict: 1
            });
        });
        
        // 地图点击事件
        this.map.on('click', function(e) {
            self.setMarker(e.lnglat.lng, e.lnglat.lat);
            self.reverseGeocode(e.lnglat.lng, e.lnglat.lat);
        });
    }
    
    loadProvinces() {
        const self = this;
        
        this.districtSearch.search('中国', function(status, result) {
            if (status === 'complete') {
                const provinces = result.districtList[0].districtList;
                const select = document.getElementById('province-select');
                
                provinces.forEach(function(province) {
                    const option = document.createElement('option');
                    option.value = province.name;
                    option.text = province.name;
                    select.appendChild(option);
                });
            }
        });
    }
    
    loadCities(provinceName) {
        const self = this;
        const select = document.getElementById('city-select');
        select.innerHTML = '<option value="">请选择城市</option>';
        
        this.districtSearch.setLevel('city');
        this.districtSearch.search(provinceName, function(status, result) {
            if (status === 'complete') {
                const cities = result.districtList[0].districtList;
                
                cities.forEach(function(city) {
                    const option = document.createElement('option');
                    option.value = city.name;
                    option.text = city.name;
                    select.appendChild(option);
                });
            }
        });
    }
    
    loadDistricts(cityName) {
        const self = this;
        const select = document.getElementById('district-select');
        select.innerHTML = '<option value="">请选择区/县</option>';
        
        this.districtSearch.setLevel('district');
        this.districtSearch.search(cityName, function(status, result) {
            if (status === 'complete') {
                const districts = result.districtList[0].districtList;
                
                districts.forEach(function(district) {
                    const option = document.createElement('option');
                    option.value = district.name;
                    option.text = district.name;
                    select.appendChild(option);
                });
            }
        });
    }
    
    locateAddress() {
        const self = this;
        
        if (!this.selectedProvince || !this.selectedCity || !this.selectedDistrict) {
            alert('请先选择省市区');
            return;
        }
        
        const fullAddress = this.selectedProvince + this.selectedCity + this.selectedDistrict + this.detailedAddress;
        
        this.geocoder.getLocation(fullAddress, function(status, result) {
            if (status === 'complete' && result.info === 'OK') {
                const location = result.geocodes[0].location;
                self.setMarker(location.lng, location.lat);
                self.map.setCenter([location.lng, location.lat]);
                self.updateAddressInfo(fullAddress);
                
                if (self.onAddressSelected) {
                    self.onAddressSelected({
                        province: self.selectedProvince,
                        city: self.selectedCity,
                        district: self.selectedDistrict,
                        detailedAddress: self.detailedAddress,
                        fullAddress: fullAddress,
                        longitude: location.lng,
                        latitude: location.lat
                    });
                }
            } else {
                alert('地址定位失败，请检查地址是否正确');
            }
        });
    }
    
    setMarker(lng, lat) {
        if (this.marker) {
            this.marker.setPosition([lng, lat]);
        } else {
            this.marker = new AMap.Marker({
                position: [lng, lat],
                map: this.map,
                icon: new AMap.Icon({
                    image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png',
                    size: new AMap.Size(19, 31),
                    imageSize: new AMap.Size(19, 31)
                })
            });
        }
        
        this.longitude = lng;
        this.latitude = lat;
    }
    
    reverseGeocode(lng, lat) {
        const self = this;
        
        this.geocoder.getAddress([lng, lat], function(status, result) {
            if (status === 'complete' && result.info === 'OK') {
                const addressComponent = result.regeocode.addressComponent;
                const formattedAddress = result.regeocode.formattedAddress;
                
                self.selectedProvince = addressComponent.province;
                self.selectedCity = addressComponent.city || addressComponent.province;
                self.selectedDistrict = addressComponent.district;
                
                // 更新下拉框
                document.getElementById('province-select').value = self.selectedProvince;
                self.loadCities(self.selectedProvince);
                
                setTimeout(function() {
                    document.getElementById('city-select').value = self.selectedCity;
                    document.getElementById('city-select').disabled = false;
                    self.loadDistricts(self.selectedCity);
                    
                    setTimeout(function() {
                        document.getElementById('district-select').value = self.selectedDistrict;
                        document.getElementById('district-select').disabled = false;
                    }, 300);
                }, 300);
                
                self.updateAddressInfo(formattedAddress);
                
                if (self.onAddressSelected) {
                    self.onAddressSelected({
                        province: self.selectedProvince,
                        city: self.selectedCity,
                        district: self.selectedDistrict,
                        detailedAddress: self.detailedAddress,
                        fullAddress: formattedAddress,
                        longitude: lng,
                        latitude: lat
                    });
                }
            }
        });
    }
    
    updateAddressInfo(address) {
        document.getElementById('current-address').textContent = address;
    }
    
    // 设置初始地址
    setAddress(addressData) {
        if (addressData.province) {
            this.selectedProvince = addressData.province;
            document.getElementById('province-select').value = addressData.province;
            this.loadCities(addressData.province);
        }
        
        if (addressData.city) {
            const self = this;
            setTimeout(function() {
                self.selectedCity = addressData.city;
                document.getElementById('city-select').value = addressData.city;
                document.getElementById('city-select').disabled = false;
                self.loadDistricts(addressData.city);
            }, 300);
        }
        
        if (addressData.district) {
            const self = this;
            setTimeout(function() {
                self.selectedDistrict = addressData.district;
                document.getElementById('district-select').value = addressData.district;
                document.getElementById('district-select').disabled = false;
            }, 600);
        }
        
        if (addressData.detailedAddress) {
            this.detailedAddress = addressData.detailedAddress;
            document.getElementById('detailed-address').value = addressData.detailedAddress;
        }
        
        if (addressData.longitude && addressData.latitude) {
            this.setMarker(addressData.longitude, addressData.latitude);
            this.map.setCenter([addressData.longitude, addressData.latitude]);
        }
    }
}

