package com.estatehub.backend.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estatehub.backend.model.entity.Property;
import com.estatehub.backend.model.entity.PropertyImage;
import com.estatehub.backend.model.entity.User;
import com.estatehub.backend.model.entity.UserProfile;
import com.estatehub.backend.model.enums.UserRoles;
import com.estatehub.backend.model.repo.PropertyRepo;
import com.estatehub.backend.model.repo.UserProfileRepo;
import com.estatehub.backend.model.repo.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepo userRepo;
    private final UserProfileRepo profileRepo;
    private final PropertyRepo propertyRepo;
    private final PasswordEncoder passwordEncoder;

    private static final List<String> SEED_EMAILS = List.of(
        "seller.yangon@estatehub.com",
        "seller.mandalay@estatehub.com",
        "seller.naypyidaw@estatehub.com"
    );

    @Override
    @Transactional
    public void run(String... args) {
        // If seed sellers already exist, clean their old properties so we can re-seed fresh
        boolean seedSellersExist = userRepo.findByEmail(SEED_EMAILS.get(0)).isPresent();

        if (seedSellersExist) {
            log.info("Seed sellers found — refreshing seed property data...");
            for (String email : SEED_EMAILS) {
                userRepo.findByEmail(email).ifPresent(seller -> {
                    var oldProps = propertyRepo.findByOwnerIdOrderByCreatedAtDesc(seller.getId());
                    if (!oldProps.isEmpty()) {
                        propertyRepo.deleteAll(oldProps);
                        log.info("  Deleted {} old properties for {}", oldProps.size(), email);
                    }
                });
            }
        } else if (propertyRepo.count() > 0) {
            log.info("Database already has non-seed properties — skipping seed.");
            return;
        }

        log.info("Seeding database with real Myanmar property data...");

        // ── Create 3 Seller Accounts ────────────────────────────────────
        var sellerYangon = findOrCreateSeller("seller.yangon@estatehub.com", "Aung Kyaw Moe",
                "Licensed real estate agent with 10+ years of experience in Yangon metropolitan area.",
                "09-123456789");

        var sellerMandalay = findOrCreateSeller("seller.mandalay@estatehub.com", "Thiha Zaw",
                "Mandalay's trusted property consultant specializing in residential and commercial properties.",
                "09-987654321");

        var sellerNpt = findOrCreateSeller("seller.naypyidaw@estatehub.com", "Hnin Wai Phyo",
                "Nay Pyi Taw real estate specialist focusing on government housing zones and new developments.",
                "09-555666777");

        // ════════════════════════════════════════════════════════════════
        //  YANGON — 16 PROPERTIES
        // ════════════════════════════════════════════════════════════════

        // 1. Condo — Star City, Thanlyin
        createProperty(sellerYangon,
                "3BR Luxury Condo in Star City, Thanlyin",
                "Stunning 3-bedroom luxury condominium in the prestigious Star City development. Features 1,600 sq ft of living space with panoramic river views, Italian marble flooring, built-in wardrobes, and a modern open-plan kitchen with European appliances. The complex offers a swimming pool, gym, 24/7 security, children's playground, and underground parking. Located just 20 minutes from downtown Yangon via the Thanlyin Bridge.",
                "CONDO", "SALE", new BigDecimal("350000000"),
                "Thanlyin", "Yangon", 16.7667, 96.2544,
                List.of(
                    "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800&q=80",
                    "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800&q=80",
                    "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800&q=80"
                ));

        // 2. Condo — Golden City, Yankin
        createProperty(sellerYangon,
                "2BR Modern Condo at Golden City, Yankin",
                "Elegant 2-bedroom unit on the 15th floor of Golden City Condominium. Offers 1,200 sq ft with floor-to-ceiling windows, air conditioning in every room, a spacious balcony, and a fully fitted kitchen. Building amenities include rooftop infinity pool, fitness center, sauna, and covered parking. Walking distance to Yankin Centre and Junction Square.",
                "CONDO", "SALE", new BigDecimal("280000000"),
                "Yankin", "Yangon", 16.8631, 96.1528,
                List.of(
                    "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=800&q=80",
                    "https://images.unsplash.com/photo-1560185127-6ed189bf02f4?w=800&q=80",
                    "https://images.unsplash.com/photo-1574362848149-11496d93a7c7?w=800&q=80"
                ));

        // 3. Condo — Crystal Residences, Kamayut
        createProperty(sellerYangon,
                "Studio Apartment at Crystal Residences, Kamayut",
                "Compact and modern studio unit in Crystal Residences, perfect for young professionals. 650 sq ft with smart storage solutions, a kitchenette with induction cooktop, rain shower bathroom, and high-speed internet wiring. Located near Hledan Junction with easy access to universities, restaurants, and public transport.",
                "CONDO", "RENT", new BigDecimal("650000"),
                "Kamayut", "Yangon", 16.8395, 96.1297,
                List.of(
                    "https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=800&q=80",
                    "https://images.unsplash.com/photo-1536376072261-38c75010e6c9?w=800&q=80"
                ));

        // 4. Condo — The Central, Sanchaung
        createProperty(sellerYangon,
                "1BR Condo at The Central Residence, Sanchaung",
                "Well-appointed 1-bedroom condominium in the heart of Sanchaung. 900 sq ft featuring hardwood floors, a modern bathroom with rain shower, split-system air conditioning, and a private balcony. The building provides elevator access, CCTV security, backup generator, and dedicated parking. Close to City Mart, KFC, and Sanchaung's vibrant food scene.",
                "CONDO", "SALE", new BigDecimal("180000000"),
                "Sanchaung", "Yangon", 16.8476, 96.1362,
                List.of(
                    "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=800&q=80",
                    "https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=800&q=80",
                    "https://images.unsplash.com/photo-1484154218962-a197022b5858?w=800&q=80"
                ));

        // 5. Condo — Shwe Hintha, Hlaing
        createProperty(sellerYangon,
                "2BR Furnished Condo at Shwe Hintha, Hlaing",
                "Fully furnished 2-bedroom condo available for immediate move-in. 1,100 sq ft with quality teak furniture, Samsung smart TV, washing machine, and a fully equipped kitchen. The unit faces Inya Lake offering serene sunset views. Complex includes swimming pool, tennis court, and 24-hour security.",
                "CONDO", "RENT", new BigDecimal("1200000"),
                "Hlaing", "Yangon", 16.8512, 96.1234,
                List.of(
                    "https://images.unsplash.com/photo-1600566753190-17f0baa2a6c3?w=800&q=80",
                    "https://images.unsplash.com/photo-1560185007-5f0bb1866cab?w=800&q=80",
                    "https://images.unsplash.com/photo-1600210491369-e753d80a41f3?w=800&q=80"
                ));

        // 6. House — Bahan (near Shwedagon)
        createProperty(sellerYangon,
                "4BR Colonial-Style Villa near Shwedagon, Bahan",
                "Magnificent 4-bedroom colonial-era villa on a quiet tree-lined street in prestigious Bahan township. 3,200 sq ft of living space on a 6,000 sq ft compound with a mature garden, covered veranda, servant quarters, and 2-car garage. Original teak staircases and high ceilings have been lovingly restored. Only 5 minutes from Shwedagon Pagoda and People's Park.",
                "HOUSE", "SALE", new BigDecimal("2500000000"),
                "Bahan", "Yangon", 16.8575, 96.1500,
                List.of(
                    "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=800&q=80",
                    "https://images.unsplash.com/photo-1600047509807-ba8f99d2cdde?w=800&q=80",
                    "https://images.unsplash.com/photo-1600585154526-990dced4db0d?w=800&q=80"
                ));

        // 7. House — Dagon Township
        createProperty(sellerYangon,
                "3BR Family Home with Garden in Dagon",
                "Charming 3-bedroom family home located in a peaceful residential area of Dagon township. 2,400 sq ft on a 4,800 sq ft plot featuring a spacious living room, separate dining area, modern kitchen, 3 bathrooms, and a landscaped garden with fruit trees. Includes a rooftop water tank, backup generator connection, and gated compound with guard house.",
                "HOUSE", "SALE", new BigDecimal("850000000"),
                "Dagon", "Yangon", 16.8661, 96.1667,
                List.of(
                    "https://images.unsplash.com/photo-1605276374104-dee2a0ed3cd6?w=800&q=80",
                    "https://images.unsplash.com/photo-1570129477492-45c003edd2be?w=800&q=80",
                    "https://images.unsplash.com/photo-1576941089067-2de3c901e126?w=800&q=80"
                ));

        // 8. House — Insein
        createProperty(sellerYangon,
                "5BR Spacious House with Large Compound, Insein",
                "Expansive 5-bedroom family residence in Insein on a generous 8,000 sq ft plot. The 3,800 sq ft home features marble-tiled floors, a grand living room, formal dining room, modern kitchen with granite countertops, master bedroom with ensuite and walk-in closet. Compound includes a detached 2-car garage, maid's quarters, and a tropical garden with gazebo.",
                "HOUSE", "SALE", new BigDecimal("1200000000"),
                "Insein", "Yangon", 16.9022, 96.1000,
                List.of(
                    "https://images.unsplash.com/photo-1583608205776-bfd35f0d9f83?w=800&q=80",
                    "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=800&q=80",
                    "https://images.unsplash.com/photo-1598228723793-52759bba239c?w=800&q=80"
                ));

        // 9. House — Tamwe (for rent)
        createProperty(sellerYangon,
                "2BR Renovated Townhouse for Rent in Tamwe",
                "Recently renovated 2-bedroom townhouse in central Tamwe, ideal for expats or small families. 1,400 sq ft over two floors with new plumbing and electrical, split AC units, western-style bathrooms, and a small courtyard. Includes fiber internet, cable TV connection, and covered motorcycle parking. Near Tamwe market, restaurants, and Kandawgyi Lake.",
                "HOUSE", "RENT", new BigDecimal("800000"),
                "Tamwe", "Yangon", 16.8558, 96.1697,
                List.of(
                    "https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=800&q=80",
                    "https://images.unsplash.com/photo-1600573472550-8090b5e0745e?w=800&q=80"
                ));

        // 10. House — Mayangone
        createProperty(sellerYangon,
                "3BR Modern House near Inya Lake, Mayangone",
                "Contemporary 3-bedroom house built in 2023 on a quiet lane in Mayangone. 2,000 sq ft of thoughtfully designed living space with an open-concept ground floor, floor-to-ceiling windows, and a rooftop terrace with Inya Lake views. Features solar water heater, smart home lighting, CCTV, and a carport for 2 vehicles. Minutes from Yangon University and the International School.",
                "HOUSE", "SALE", new BigDecimal("1800000000"),
                "Mayangone", "Yangon", 16.8617, 96.1194,
                List.of(
                    "https://images.unsplash.com/photo-1613490493576-7fde63acd811?w=800&q=80",
                    "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800&q=80",
                    "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=800&q=80"
                ));

        // 11. Apartment — Downtown (Pabedan)
        createProperty(sellerYangon,
                "2BR Apartment in Downtown Yangon, Pabedan",
                "Bright 2-bedroom apartment on the 6th floor (with lift) in the heart of downtown Pabedan. 950 sq ft featuring polished cement floors, large windows overlooking Mahabandula Park, two air-conditioned bedrooms, and a sitting/dining area. Shared rooftop with city views. Steps from Sule Pagoda, City Hall, and the Strand Hotel.",
                "APARTMENT", "RENT", new BigDecimal("500000"),
                "Pabedan", "Yangon", 16.8662, 96.1583,
                List.of(
                    "https://images.unsplash.com/photo-1502672023488-70e25813eb80?w=800&q=80",
                    "https://images.unsplash.com/photo-1560185007-cde436f6a4d0?w=800&q=80"
                ));

        // 12. Apartment — Latha
        createProperty(sellerYangon,
                "1BR Apartment in Chinatown, Latha",
                "Cozy 1-bedroom apartment in Yangon's vibrant Chinatown district. 600 sq ft on the 4th floor (walk-up) with a renovated bathroom, ceiling fans, and a compact kitchen. Perfect for singles or couples who want to be in the center of Yangon's best street food scene. Near 19th Street BBQ strip, Theingyi Market, and multiple bus routes.",
                "APARTMENT", "RENT", new BigDecimal("350000"),
                "Latha", "Yangon", 16.8633, 96.1539,
                List.of(
                    "https://images.unsplash.com/photo-1630699144867-37acec97df5a?w=800&q=80",
                    "https://images.unsplash.com/photo-1554995207-c18c203602cb?w=800&q=80"
                ));

        // 13. Apartment — Kyauktada
        createProperty(sellerYangon,
                "3BR Apartment near Bogyoke Market, Kyauktada",
                "Spacious 3-bedroom apartment in a well-maintained building near Bogyoke Aung San Market. 1,300 sq ft with tiled floors, 3 split ACs, 2 bathrooms, a large living room, and a separate kitchen with exhaust hood. Ideal for families who want downtown convenience. Close to Bogyoke Market, Sakura Tower, and the Central Railway Station.",
                "APARTMENT", "SALE", new BigDecimal("150000000"),
                "Kyauktada", "Yangon", 16.8580, 96.1560,
                List.of(
                    "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?w=800&q=80",
                    "https://images.unsplash.com/photo-1600607687644-c7171b42498f?w=800&q=80",
                    "https://images.unsplash.com/photo-1556020685-ae41abfc9365?w=800&q=80"
                ));

        // 14. Apartment — Mingalar Taung Nyunt
        createProperty(sellerYangon,
                "2BR Serviced Apartment, Mingalar Taung Nyunt",
                "Modern serviced 2-bedroom apartment near Kandawgyi Lake. 1,000 sq ft with hotel-quality furnishings, a fully equipped kitchen, washer/dryer, high-speed WiFi, and weekly housekeeping included in rent. Building has a lobby reception, elevator, rooftop garden, and secure parking. Popular with diplomats and NGO staff.",
                "APARTMENT", "RENT", new BigDecimal("1500000"),
                "Mingalar Taung Nyunt", "Yangon", 16.8536, 96.1650,
                List.of(
                    "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=800&q=80",
                    "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea?w=800&q=80"
                ));

        // 15. Land — South Dagon
        createProperty(sellerYangon,
                "40x60 Residential Land Plot in South Dagon",
                "Prime residential land plot measuring 40x60 feet (2,400 sq ft) in a developing area of South Dagon. Flat terrain with road access on one side, electricity connection available, and municipal water supply nearby. Ideal for building a family home or small apartment building. Title deed (Grant) is clean and ready for transfer. Area is growing rapidly with new infrastructure projects.",
                "LAND", "SALE", new BigDecimal("180000000"),
                "South Dagon", "Yangon", 16.8333, 96.2167,
                List.of(
                    "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=800&q=80",
                    "https://images.unsplash.com/photo-1628624747186-a941c476b7ef?w=800&q=80"
                ));

        // 16. Land — Hmawbi
        createProperty(sellerYangon,
                "1 Acre Agricultural Land in Hmawbi",
                "One acre of fertile agricultural land located along the Yangon-Pyay highway near Hmawbi. Currently used for rice cultivation with irrigation canal access. Suitable for farming, warehouse development, or long-term investment as the Yangon metropolitan area expands northward. Clean land grant title with no encumbrances. Electricity poles on boundary.",
                "LAND", "SALE", new BigDecimal("500000000"),
                "Hmawbi", "Yangon", 17.1000, 96.0667,
                List.of(
                    "https://images.unsplash.com/photo-1625246333195-78d9c38ad449?w=800&q=80",
                    "https://images.unsplash.com/photo-1595841696677-6589b53f8b3e?w=800&q=80"
                ));

        // ════════════════════════════════════════════════════════════════
        //  MANDALAY — 6 PROPERTIES
        // ════════════════════════════════════════════════════════════════

        // 17. Condo — Chanayethazan
        createProperty(sellerMandalay,
                "2BR Condo near Mandalay Palace, Chanayethazan",
                "Modern 2-bedroom condominium in the prime Chanayethazan district, just minutes from the Mandalay Royal Palace and moat. 1,050 sq ft with contemporary finishes, air conditioning, a fitted kitchen, and a balcony with palace wall views. Building has elevator, generator backup, and guarded entrance. Walking distance to Zegyo Market and the Central Business District.",
                "CONDO", "SALE", new BigDecimal("200000000"),
                "Chanayethazan", "Mandalay", 21.9588, 96.0891,
                List.of(
                    "https://images.unsplash.com/photo-1567496898669-ee935f5f647a?w=800&q=80",
                    "https://images.unsplash.com/photo-1600573472592-401b489a3cdc?w=800&q=80",
                    "https://images.unsplash.com/photo-1560440021-33f9b867899d?w=800&q=80"
                ));

        // 18. Condo — Aungmyethazan
        createProperty(sellerMandalay,
                "1BR Furnished Condo for Rent, Aungmyethazan",
                "Tastefully furnished 1-bedroom condo in a new development in Aungmyethazan. 750 sq ft with queen bed, sofa set, dining table, refrigerator, and microwave. Modern bathroom with hot water shower. Building features a rooftop lounge, laundry room, and visitor parking. Close to the university, hospitals, and the famous Mandalay Hill.",
                "CONDO", "RENT", new BigDecimal("450000"),
                "Aungmyethazan", "Mandalay", 21.9700, 96.1000,
                List.of(
                    "https://images.unsplash.com/photo-1600585152220-90363fe7e115?w=800&q=80",
                    "https://images.unsplash.com/photo-1600121848594-d8644e57abab?w=800&q=80"
                ));

        // 19. House — Central Mandalay
        createProperty(sellerMandalay,
                "4BR Traditional Teak House, Central Mandalay",
                "Exquisite 4-bedroom traditional Mandalay teak house with intricate wood carvings and a spacious courtyard. 2,800 sq ft of living space on a 5,500 sq ft lot. Features a formal reception hall, family room, large kitchen, and a covered outdoor dining area. The compound includes a teak gazebo, fish pond, and flowering garden. A rare cultural gem in the heart of Mandalay.",
                "HOUSE", "SALE", new BigDecimal("1500000000"),
                "Chanayethazan", "Mandalay", 21.9625, 96.0850,
                List.of(
                    "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800&q=80",
                    "https://images.unsplash.com/photo-1599427303058-f04cbcf4756f?w=800&q=80",
                    "https://images.unsplash.com/photo-1595521624992-48a59aef95e3?w=800&q=80"
                ));

        // 20. House — Mahaaungmyay
        createProperty(sellerMandalay,
                "3BR Modern Villa with Pool, Mahaaungmyay",
                "Newly built 3-bedroom modern villa with a private swimming pool in upscale Mahaaungmyay. 2,200 sq ft with open-plan living, imported tiles, a designer kitchen with island counter, master bedroom with jacuzzi bath, and a landscaped garden. Includes solar panels, CCTV, intercom, and a 2-car garage. Near international schools and Mandalay International Airport road.",
                "HOUSE", "SALE", new BigDecimal("2000000000"),
                "Mahaaungmyay", "Mandalay", 21.9453, 96.0858,
                List.of(
                    "https://images.unsplash.com/photo-1600566753376-12c8ab7c5a38?w=800&q=80",
                    "https://images.unsplash.com/photo-1600047509358-9dc75507daeb?w=800&q=80",
                    "https://images.unsplash.com/photo-1600566753151-384129cf4e3e?w=800&q=80"
                ));

        // 21. Apartment — Chanayethazan
        createProperty(sellerMandalay,
                "2BR Apartment near Zegyo Market, Chanayethazan",
                "Well-maintained 2-bedroom apartment on the 5th floor in the commercial hub near Zegyo Market. 900 sq ft with tiled floors, 2 ACs, attached bathrooms, and a balcony facing the busy market street. Excellent for anyone working in central Mandalay. Near banks, clinics, and Mandalay's best mohinga shops.",
                "APARTMENT", "RENT", new BigDecimal("400000"),
                "Chanayethazan", "Mandalay", 21.9595, 96.0920,
                List.of(
                    "https://images.unsplash.com/photo-1600210491892-03d54c0aaf87?w=800&q=80",
                    "https://images.unsplash.com/photo-1600607688969-a5bfcd646154?w=800&q=80"
                ));

        // 22. Land — Amarapura
        createProperty(sellerMandalay,
                "60x80 Commercial Land Plot, Amarapura",
                "Strategic 60x80 feet commercial land plot on the main road in Amarapura, famous for the U Bein Bridge. 4,800 sq ft with dual road access, suitable for a hotel, restaurant, guesthouse, or retail development. High tourist foot traffic area. All utilities available. Grant title with clean history. Excellent investment opportunity in Mandalay's tourism corridor.",
                "LAND", "SALE", new BigDecimal("800000000"),
                "Amarapura", "Mandalay", 21.8970, 96.0590,
                List.of(
                    "https://images.unsplash.com/photo-1524813686514-a57563d77965?w=800&q=80",
                    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&q=80"
                ));

        // ════════════════════════════════════════════════════════════════
        //  NAY PYI TAW — 3 PROPERTIES
        // ════════════════════════════════════════════════════════════════

        // 23. Condo — Zabuthiri
        createProperty(sellerNpt,
                "2BR Government Zone Condo, Zabuthiri",
                "Modern 2-bedroom condominium in the Zabuthiri government housing zone. 1,000 sq ft with contemporary design, air conditioning, fitted wardrobes, and a compact but functional kitchen. The development includes gardens, a community hall, convenience store, and 24-hour security. Ideal for government employees and civil servants. Close to ministries and the Nay Pyi Taw Council area.",
                "CONDO", "SALE", new BigDecimal("120000000"),
                "Zabuthiri", "Nay Pyi Taw", 19.7633, 96.0785,
                List.of(
                    "https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?w=800&q=80",
                    "https://images.unsplash.com/photo-1600585153490-76fb20a32601?w=800&q=80",
                    "https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?w=800&q=80"
                ));

        // 24. House — Ottarathiri
        createProperty(sellerNpt,
                "4BR Detached House with Garden, Ottarathiri",
                "Spacious 4-bedroom detached house on a generous 7,000 sq ft plot in the quiet Ottarathiri township. 2,600 sq ft of living space featuring a large living room, family dining area, modern kitchen with storage pantry, master suite with walk-in closet, and 3 additional bedrooms. The walled compound has a lush garden, mango and jackfruit trees, a 2-car garage, and servant quarters. Perfect for families seeking space and tranquility.",
                "HOUSE", "SALE", new BigDecimal("650000000"),
                "Ottarathiri", "Nay Pyi Taw", 19.8700, 96.1300,
                List.of(
                    "https://images.unsplash.com/photo-1600573472591-ee6b68d14c68?w=800&q=80",
                    "https://images.unsplash.com/photo-1609347744403-2306e8a9ae27?w=800&q=80",
                    "https://images.unsplash.com/photo-1600563438938-a9a27216b4f5?w=800&q=80"
                ));

        // 25. Land — Dekkhina
        createProperty(sellerNpt,
                "100x100 Development Land in Dekkhina",
                "Large 100x100 feet (10,000 sq ft) development land in Dekkhina township, one of Nay Pyi Taw's fastest growing districts. Flat and cleared, with road frontage, municipal water connection, and 3-phase electricity at the boundary. Zoned for mixed-use development — ideal for a housing project, hotel, or commercial complex. Title is government-issued grant with all clearances.",
                "LAND", "SALE", new BigDecimal("350000000"),
                "Dekkhina", "Nay Pyi Taw", 19.7200, 96.1300,
                List.of(
                    "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=800&q=80",
                    "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=800&q=80"
                ));

        log.info("✅ Seeded 25 properties with unique images successfully!");
    }

    // ── Helper: find existing seller or create new ──────────────────────
    private User findOrCreateSeller(String email, String fullName, String bio, String phone) {
        var existing = userRepo.findByEmail(email);
        if (existing.isPresent()) {
            log.info("  Seller already exists: {}", email);
            return existing.get();
        }

        var user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(UserRoles.SELLER);
        var savedUser = userRepo.save(user);

        var profile = new UserProfile();
        profile.setUser(savedUser);
        profile.setFullName(fullName);
        profile.setBio(bio);
        profile.setPhone(phone);
        profileRepo.save(profile);

        log.info("  Created seller: {} ({})", fullName, email);
        return savedUser;
    }

    // ── Helper: create a property with images ───────────────────────────
    private void createProperty(User owner, String title, String description,
                                 String propertyType, String listingType, BigDecimal price,
                                 String township, String city, double lat, double lng,
                                 List<String> imageUrls) {
        var property = new Property();
        property.setTitle(title);
        property.setDescription(description);
        property.setPropertyType(propertyType);
        property.setListingType(listingType);
        property.setPrice(price);
        property.setTownship(township);
        property.setCity(city);
        property.setLatitude(lat);
        property.setLongitude(lng);
        property.setStatus("AVAILABLE");
        property.setOwner(owner);
        property.setViewCount(0);

        // Add images — first one is the cover
        for (int i = 0; i < imageUrls.size(); i++) {
            var img = new PropertyImage();
            img.setImageUrl(imageUrls.get(i));
            img.setCover(i == 0);
            img.setProperty(property);
            property.getImages().add(img);
        }

        propertyRepo.save(property);
    }
}
