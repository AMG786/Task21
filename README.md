# Unit Converter App
A modern and user-friendly Android application that converts between different units of measurement.


![image](https://github.com/user-attachments/assets/fac44572-03a8-4df4-b2e4-10125ec7f20d)
![image](https://github.com/user-attachments/assets/48f07e38-be33-4a42-89ea-9d3280bd7240)

## Features
- Convert between various units in three categories:
  - **Length**: Centimeter, Meter, Kilometer, Inch, Foot, Yard, Mile
  - **Weight**: Gram, Kilogram, Metric Ton, Ounce, Pound, Ton
  - **Temperature**: Celsius, Fahrenheit, Kelvin
- Clean, intuitive user interface with Material Design components
- Accurate conversion calculations
- Formatted results with proper decimal precision
- Comprehensive error handling and input validation

## How to Use
1. Select a conversion category (Length, Weight, or Temperature)
2. Enter a value in the input field
3. Select the source unit from the "From" dropdown
4. Select the target unit from the "To" dropdown
5. Tap the "CONVERT" button to see the result

## Error Handling
The application includes robust error handling to ensure a smooth user experience:

### Input Validation
- **Empty Input**: Displays an error message when the input field is left empty
- **Invalid Number Format**: Validates that inputs are proper numerical values
- **Negative Value Validation**: Prevents negative values for units where they don't make sense (e.g., Kelvin temperature)

### User Feedback
- **Visual Indicators**: Error messages are displayed in red to alert users
- **Field-Specific Errors**: Input field shows error state with messages explaining the issue
- **Automatic Error Clearing**: Errors are automatically cleared when users begin typing
- **Informative Messages**: Clear error messages explain exactly what went wrong

### Edge Cases
- **Same Unit Conversion**: Handles cases where source and destination units are the same
- **Floating Point Precision**: Addresses potential floating-point precision issues in calculations
- **Result Formatting**: Intelligently formats results to remove unnecessary trailing zeros

## Implementation Details
- Built with Kotlin for Android
- Uses Material Design components
- Implements conversion logic using a base unit approach for accuracy
- Handles edge cases and input validation
- Uses TextWatcher to provide real-time feedback on input errors

## Technical Requirements
- Android 5.0 (API level 21) or higher
- Kotlin support

## Future Enhancements
- Add more conversion categories
- Save conversion history
- Copy results to clipboard
- Support for dark mode
- Enhanced error handling with more specific feedback
- Input range validation for each unit type

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/your-repo.git
   ```
2. Navigate to the project directory:
   ```sh
   cd your-repo
   ```
3. Install dependencies:
   ```sh
   npm install  # or pip install -r requirements.txt
   ```

## Usage
Provide instructions on how to run the project:
```sh
npm start  # or python main.py
```

## Contributing
1. Fork the repository
2. Create a new branch (`git checkout -b feature-branch`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature-branch`)
5. Open a Pull Request

## Contact
For any inquiries, contact [abdulmueez917@gmail.com](abdulmueez917@gmail.com)
